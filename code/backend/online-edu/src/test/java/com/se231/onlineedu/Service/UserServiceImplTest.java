package com.se231.onlineedu.Service;


import com.se231.onlineedu.exception.*;
import com.se231.onlineedu.message.request.SignInUserForm;
import com.se231.onlineedu.message.response.PersonalInfo;
import com.se231.onlineedu.model.*;
import com.se231.onlineedu.repository.RoleRepository;
import com.se231.onlineedu.repository.SignInRepository;
import com.se231.onlineedu.repository.UserRepository;
import com.se231.onlineedu.service.CourseService;
import com.se231.onlineedu.service.UserService;
import com.se231.onlineedu.serviceimpl.UserServiceImpl;
import org.assertj.core.util.DateUtil;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;

@RunWith(SpringRunner.class)
public class UserServiceImplTest {
    @TestConfiguration
    static class UserServiceImplTestContextConfig{
        @Bean
        public UserService userService(){
            return new UserServiceImpl();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

    }
    @Autowired
    UserService userService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    private PasswordEncoder encoder;

    @MockBean
    private RoleRepository roleRepository;


    @MockBean
    private SignInRepository signInRepository;

    @MockBean
    private CourseService courseService;

    @MockBean
    UserRepository userRepository;


    @Test(expected = NotFoundException.class)
    public void UserNotFound() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());
        userService.getUserInfo(1L);
    }

    @Test
    public void getUserInfo()  {
        User user = new User();
        user.setTel(531L);
        user.setUsername("Liu");
        user.setEmail("cdjddzy@foxmail.com");
        user.setPassword("password");
        user.setId(1L);
        Optional<User> userOptional = Optional.of(user);

        Mockito.when(userRepository.findById(1L)).thenReturn(userOptional);

        User found = userService.getUserInfo(1L);
        assertThat(found.getEmail()).isEqualTo("cdjddzy@foxmail.com");
    }

    @Test(expected = NotFoundException.class)
    public void RoleNotFound() throws IOException {
        Mockito.when(roleRepository.findByRole(RoleType.ROLE_USER)).thenReturn(Optional.empty());

        File emptyFile = new File("src/test/resources/UserData.xlsx");
        MockMultipartFile multipartFile = new MockMultipartFile("excel", "UserData.xlsx","plain/text", new FileInputStream(emptyFile.getPath()));

        userService.bulkImportUser(multipartFile);
    }

    @Test(expected = EmptyFileException.class)
    public void bulkImportUserEmptyFile() throws IOException {
        File emptyFile = new File("src/test/resources/UserDataEmpty.xlsx");
        MockMultipartFile multipartFile = new MockMultipartFile("excel", "UserData.xlsx","plain/text", new FileInputStream(emptyFile.getPath()));

        userService.bulkImportUser(multipartFile);
    }

    @Test(expected = FileFormatNotSupportException.class)
    public void bulkImportUserWrongFormatFile() throws IOException {
        File emptyFile = new File("src/test/resources/UserDataEmpty.xlsx");
        MockMultipartFile multipartFile = new MockMultipartFile("excel", "UserData.l4sx","plain/text", new FileInputStream(emptyFile.getPath()));

        userService.bulkImportUser(multipartFile);
    }

    @Test
    public void bulkImportUser() throws IOException {
        File emptyFile = new File("src/test/resources/UserData.xlsx");
        MockMultipartFile multipartFile = new MockMultipartFile("excel", "UserData.xlsx","plain/text", new FileInputStream(emptyFile.getPath()));
        Role role = new Role(RoleType.ROLE_USER);
        Optional<Role> roleOptional = Optional.of(role);
        Mockito.when(roleRepository.findByRole(any(RoleType.class))).thenReturn(roleOptional);

        String response = userService.bulkImportUser(multipartFile);
        assertThat(response).isEqualTo("导入成功");
    }

    @Test
    public void bulkImportUserSameEmail() throws IOException {
        expectedException.expect(BulkImportDataException.class);
        expectedException.expectMessage("Data Error -> Same Email In Row");

        Mockito.when(userRepository.existsByEmail(any(String.class))).thenReturn(true);
        Role role = new Role(RoleType.ROLE_USER);
        Optional<Role> roleOptional = Optional.of(role);
        Mockito.when(roleRepository.findByRole(any(RoleType.class))).thenReturn(roleOptional);

        File emptyFile = new File("src/test/resources/UserDataSameEmail.xlsx");
        MockMultipartFile multipartFile = new MockMultipartFile("excel", "UserData.xlsx","plain/text", new FileInputStream(emptyFile.getPath()));

        userService.bulkImportUser(multipartFile);
    }

    @Test
    public void bulkImportUserSameTel() throws IOException {
        expectedException.expect(BulkImportDataException.class);
        expectedException.expectMessage("Data Error -> Same Telephone Number In Row");
        Role role = new Role(RoleType.ROLE_USER);
        Optional<Role> roleOptional = Optional.of(role);
        Mockito.when(roleRepository.findByRole(any(RoleType.class))).thenReturn(roleOptional);
        Mockito.when(userRepository.existsByTel(any(Long.class))).thenReturn(true);

        File emptyFile = new File("src/test/resources/UserDataSameTel.xlsx");
        MockMultipartFile multipartFile = new MockMultipartFile("excel", "UserData.xlsx","plain/text", new FileInputStream(emptyFile.getPath()));

        userService.bulkImportUser(multipartFile);
    }

    @Test
    public void bulkImportUserSameUsername() throws IOException {
        expectedException.expect(BulkImportDataException.class);
        expectedException.expectMessage("Data Error -> Same Username In Row");

        Role role = new Role(RoleType.ROLE_USER);
        Optional<Role> roleOptional = Optional.of(role);
        Mockito.when(roleRepository.findByRole(any(RoleType.class))).thenReturn(roleOptional);
        Mockito.when(userRepository.existsByUsername(any(String.class))).thenReturn(true);

        File emptyFile = new File("src/test/resources/UserDataSameUsername.xlsx");
        MockMultipartFile multipartFile = new MockMultipartFile("excel", "UserData.xlsx","plain/text", new FileInputStream(emptyFile.getPath()));

        userService.bulkImportUser(multipartFile);
    }

    @Test
    public void manageUserInfo(){
        PersonalInfo personalInfo = new PersonalInfo();
        personalInfo.setRealName("haha");
        personalInfo.setTel("1235646");

        User user = new User();
        user.setTel(531L);
        user.setUsername("Liu");
        user.setEmail("cdjddzy@foxmail.com");
        user.setPassword("password");
        user.setId(1L);
        Optional<User> userOptional = Optional.of(user);

        Mockito.when(userRepository.findById(1L)).thenReturn(userOptional);
        Mockito.when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User updated =  userService.manageUserInfo(1L, personalInfo);
        assertThat(updated.getRealName()).isEqualTo("haha");
    }

    @Test
    public void getAllUser(){
        User user = new User();
        user.setTel(531L);
        user.setUsername("Liu");
        user.setEmail("cdjddzy@foxmail.com");
        user.setPassword("password");
        user.setId(1L);
        List<User> users = new ArrayList<>();
        users.add(user);

        Mockito.when(userRepository.findAll()).thenReturn(users);

        List<User> found = userService.getAllUser();
        assertThat(found.size()).isEqualTo(1);
        assertThat(found.get(0).getUsername()).isEqualTo("Liu");
    }

    @Test
    public void checkSameUsername(){
        Mockito.when(userRepository.existsByUsername(anyString())).thenReturn(true);
        assertThat(userService.checkSameUsername("aa")).isEqualTo(true);
    }

    @Test
    public void checkSameTel(){
        Mockito.when(userRepository.existsByTel(anyLong())).thenReturn(true);
        assertThat(userService.checkSameTel("1234")).isEqualTo(true);
    }

    @Test
    public void checkSameEmail(){
        Mockito.when(userRepository.existsByEmail(anyString())).thenReturn(true);
        assertThat(userService.checkSameEmail("1234")).isEqualTo(true);
    }

    @Test
    public void updateUserAvatar(){
        User user = new User();
        user.setTel(531L);
        user.setUsername("Liu");
        user.setEmail("cdjddzy@foxmail.com");
        user.setPassword("password");
        user.setId(1L);
        Optional<User> userOptional = Optional.of(user);

        Mockito.when(userRepository.findById(1L)).thenReturn(userOptional);
        Mockito.when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);
        String url = userService.updateUserAvatar("hehe", 1L);

        assertThat(url).isEqualTo("hehe");
    }

    @Test
    public void updateUserPasswordConfirm(){
        User user = new User();
        user.setTel(531L);
        user.setUsername("Liu");
        user.setEmail("cdjddzy@foxmail.com");
        user.setPassword("password");
        user.setId(1L);
        Optional<User> userOptional = Optional.of(user);

        Mockito.when(userRepository.findById(1L)).thenReturn(userOptional);
        Mockito.when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);
        User found = userService.updateUserPasswordConfirm( 1L,"Password");
        assertThat(encoder.matches("Password", found.getPassword())).isEqualTo(true);
    }

    @Test
    public void updateUserEmailConfirm(){
        User user = new User();
        user.setTel(531L);
        user.setUsername("Liu");
        user.setEmail("cdjddzy@foxmail.com");
        user.setPassword("password");
        user.setId(1L);
        Optional<User> userOptional = Optional.of(user);

        Mockito.when(userRepository.findById(1L)).thenReturn(userOptional);
        Mockito.when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);
        User found = userService.updateUserEmailConfirm( 1L,"Password");
        assertThat(found.getEmail()).isEqualTo("Password");
    }

    @Test(expected = NotFoundException.class)
    public void saveUserSignInNotExist(){
        User user = new User();
        Optional<User> userOptional = Optional.of(user);
        SignInUserForm signInUserForm = new SignInUserForm();
        Mockito.when(userRepository.findById(1L)).thenReturn(userOptional);
        Mockito.when(signInRepository.findById(any(SignInPrimaryKey.class))).thenReturn(Optional.empty());
        userService.saveUserSignIn(1L, signInUserForm);
    }

    @Test(expected = BeforeStartException.class)
    public void saveUserSignInBeforeStart(){
        User user = new User();
        Optional<User> userOptional = Optional.of(user);
        SignInUserForm signInUserForm = new SignInUserForm();
        SignIn signIn = new SignIn();
        signIn.setStartDate(DateUtil.tomorrow());
        Mockito.when(userRepository.findById(1L)).thenReturn(userOptional);
        Mockito.when(signInRepository.findById(any(SignInPrimaryKey.class))).thenReturn(Optional.of(signIn));
        userService.saveUserSignIn(1L,signInUserForm);
    }

    @Test(expected = AfterEndException.class)
    public void saveUserSignInAfterEnd(){
        User user = new User();
        Optional<User> userOptional = Optional.of(user);
        SignInUserForm signInUserForm = new SignInUserForm();
        SignIn signIn = new SignIn();
        signIn.setStartDate(new Date());
        signIn.setEndDate(DateUtil.yesterday());
        Mockito.when(userRepository.findById(1L)).thenReturn(userOptional);
        Mockito.when(signInRepository.findById(any(SignInPrimaryKey.class))).thenReturn(Optional.of(signIn));
        userService.saveUserSignIn(1L,signInUserForm);
    }

    @Test
    public void saveUserSignIn(){
        User user = new User();
        Optional<User> userOptional = Optional.of(user);
        SignInUserForm signInUserForm = new SignInUserForm();
        signInUserForm.setCourseId(1L);
        signInUserForm.setSignInNo(1);
        signInUserForm.setLatitude(0D);
        signInUserForm.setLongitude(0D);
        SignIn signIn = new SignIn();
        signIn.setStartDate(DateUtil.yesterday());
        signIn.setEndDate(DateUtil.tomorrow());
        signIn.setLatitude(0D);
        signIn.setLongitude(0D);
        signIn.setSignInPrimaryKey(new SignInPrimaryKey(new Course(),12));
        Mockito.when(userRepository.findById(1L)).thenReturn(userOptional);
        Mockito.when(signInRepository.findById(any(SignInPrimaryKey.class))).thenReturn(Optional.of(signIn));
        Mockito.when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);
        User found = userService.saveUserSignIn(1L,signInUserForm);
        assertThat(found.getSignIns().get(0).getSignInPrimaryKey().getSignInNo()).isEqualTo(12);
    }

    @Test
    public void checkIfSameAsOldPassword(){
        User user = new User();
        System.out.println(encoder.encode("123456"));
        user.setPassword(encoder.encode("123456"));
        Optional<User> userOptional = Optional.of(user);

        Mockito.when(userRepository.findById(1L)).thenReturn(userOptional);
        boolean shouldTrue = userService.checkIfSameAsOldPassword(1L,"123456");
        assertThat(shouldTrue).isEqualTo(true);
    }

    @Test
    public void checkIfSameAsOldEmail(){
        User user = new User();
        user.setEmail("123456789");
        Optional<User> userOptional = Optional.of(user);

        Mockito.when(userRepository.findById(1L)).thenReturn(userOptional);
        boolean shouldTrue = userService.checkIfSameAsOldEmail(1L,"123456789");
        assertThat(shouldTrue).isEqualTo(true);
    }

    @Test
    public void checkIfSameAsOldTel(){
        User user = new User();
        user.setTel(123456789L);
        Optional<User> userOptional = Optional.of(user);

        Mockito.when(userRepository.findById(1L)).thenReturn(userOptional);
        boolean shouldTrue = userService.checkIfSameAsOldTel(1L,123456789L);
        assertThat(shouldTrue).isEqualTo(true);
    }



}
