package com.se231.onlineedu.serviceimpl;

import com.alibaba.excel.EasyExcelFactory;
import com.se231.onlineedu.exception.*;
import com.se231.onlineedu.message.request.SignInUserForm;
import com.se231.onlineedu.message.request.UserExcel;
import com.se231.onlineedu.message.response.PersonalInfo;
import com.se231.onlineedu.message.response.SignInWithState;
import com.se231.onlineedu.message.response.UserAvatar;
import com.se231.onlineedu.model.*;
import com.se231.onlineedu.repository.RoleRepository;
import com.se231.onlineedu.repository.SignInRepository;
import com.se231.onlineedu.repository.UserRepository;
import com.se231.onlineedu.service.CourseService;
import com.se231.onlineedu.service.UserService;
import com.se231.onlineedu.util.FileCheckUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



/**
 * User Service Implementation Class
 *
 * contain main service logic related to user.
 *
 * @author Zhe Li
 *
 * @date 2019/07/08
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SignInRepository signInRepository;

    @Autowired
    private CourseService courseService;

    @Override
    public User getUserInfo(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("该用户不存在"));
        return user;
    }

    @Override
    public User manageUserInfo(Long id, PersonalInfo personalInfo){
        User user = getUserInfo(id);
        personalInfo.modifyUserInfo(user);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public boolean checkSameUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean checkSameEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean checkSameTel(String tel) {
        return userRepository.existsByTel(Long.parseLong(tel));
    }

    @Override
    public String bulkImportUser(MultipartFile excel) throws IOException {
        FileCheckUtil.checkExcelValid(excel);

        List<Role> roles=new ArrayList<>();
        Role userRole = roleRepository.findByRole(RoleType.ROLE_USER).orElseThrow(()->new NotFoundException("该角色不存在"));
        roles.add(userRole);

        InputStream file = excel.getInputStream();
        List<Object> data = EasyExcelFactory
                .read(file, new com.alibaba.excel.metadata.Sheet(1,1, UserExcel.class));
        int rowNumber=1;
        boolean hasError = false;
        StringBuilder errorMessage= new StringBuilder();
        errorMessage.append("Some Data Has Error:\n");
        //username,password,email,tel,university,major,sno,grade,real name,sex
        for(Object dataItem:data){
            rowNumber++;
            UserExcel userExcel=(UserExcel)dataItem;
            if(userExcel.getPassword() == null){
                continue;
            }
            if(userRepository.existsByUsername(userExcel.getUsername())){
                hasError=true;
                errorMessage.append("Data Error -> Same Username In Row "+rowNumber+"\n");
                continue;
            }
            if(userRepository.existsByEmail(userExcel.getEmail())){
                hasError=true;
                errorMessage.append("Data Error -> Same Email In Row "+rowNumber+"\n");
                continue;
            }
            if(userRepository.existsByTel(userExcel.getTel())){
                hasError=true;
                errorMessage.append("Data Error -> Same Telephone Number In Row "+rowNumber+"\n");
                continue;
            }
            User user =new User(userExcel);
            user.setRoles(roles);
            user.setPassword(passwordEncoder.encode(userExcel.getPassword()));
            userRepository.save(user);
        }

        if(!hasError) {
            return "导入成功";
        }
        else {
            throw new BulkImportDataException(errorMessage.toString());
        }
    }

    @Override
    public String updateUserAvatar(String avatarUrl, Long id) {
        User user = getUserInfo(id);
        user.setAvatarUrl(avatarUrl);
        userRepository.save(user);
        return avatarUrl;
    }

    @Override
    public User updateUserPasswordConfirm(Long id, String password) {
        User user = getUserInfo(id);
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    @Override
    public User updateUserEmailConfirm(Long id, String email) {
        User user = getUserInfo(id);
        user.setEmail(email);
        return userRepository.save(user);
    }

    @Override
    public User saveUserSignIn(Long id, SignInUserForm signInUserForm){
        User user = getUserInfo(id);
        SignInPrimaryKey signInPrimaryKey = new SignInPrimaryKey(courseService.getCourseInfo(signInUserForm.getCourseId()),signInUserForm.getSignInNo());
        SignIn signIn = signInRepository.findById(signInPrimaryKey).orElseThrow(()-> new NotFoundException("该签到不存在"));
        Date date = new Date();
        if(date.before(signIn.getStartDate())){
            throw new BeforeStartException("签到还未开始");
        }
        if(date.after(signIn.getEndDate())){
            throw new AfterEndException("签到已经结束");
        }
        GlobalCoordinates source = new GlobalCoordinates(signIn.getLatitude(),signIn.getLongitude());
        GlobalCoordinates target = new GlobalCoordinates(signInUserForm.getLatitude(),signInUserForm.getLongitude());
        if(getDistanceMeter(source, target, Ellipsoid.Sphere) > 5000D){
            throw new ValidationException("距离过远，请重新签到");
        }
        signIn.getUsers().add(user);
        signInRepository.save(signIn);
        user.getSignIns().add(signIn);
        return userRepository.save(user);
    }

    public static double getDistanceMeter(GlobalCoordinates gpsFrom, GlobalCoordinates gpsTo, Ellipsoid ellipsoid)
    {
        //创建GeodeticCalculator，调用计算方法，传入坐标系、经纬度用于计算距离
        GeodeticCurve geoCurve = new GeodeticCalculator().calculateGeodeticCurve(ellipsoid, gpsFrom, gpsTo);

        return geoCurve.getEllipsoidalDistance();
    }

    @Override
    public boolean checkIfSameAsOldPassword(Long id, String password) {
        User user = getUserInfo(id);
        return passwordEncoder.matches(password, user.getPassword());
    }

    @Override
    public boolean checkIfSameAsOldTel(Long id,Long tel) {
        User user = getUserInfo(id);
        return tel.equals(user.getTel());
    }

    @Override
    public boolean checkIfSameAsOldEmail(Long id, String Email){
        User user = getUserInfo(id);
        return Email.equals(user.getEmail());
    }

    @Override
    public UserAvatar getUserAvatar(Long userId) {
        User user = getUserInfo(userId);
        return new UserAvatar(user.getUsername(),user.getAvatarUrl());
    }

    @Override
    public List<SignInWithState> getUserSignIns(Long courseId, Long userId){
        List<SignIn> signInsCourse = signInRepository.findBySignInPrimaryKey_Course_Id(courseId);
        List<SignIn> signInsUser = getUserInfo(userId).getSignIns();
        List<SignInWithState> signInWithStates = new ArrayList();
        for(SignIn signIn: signInsCourse){
            if(signInsUser.contains(signIn)){
                signInWithStates.add(new SignInWithState(signIn, true));
            } else {
                signInWithStates.add(new SignInWithState(signIn, false));
            }
        }
        return signInWithStates;

    }
}
