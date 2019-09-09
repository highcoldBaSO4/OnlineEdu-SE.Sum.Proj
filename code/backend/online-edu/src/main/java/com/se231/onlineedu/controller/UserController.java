package com.se231.onlineedu.controller;

import com.alibaba.fastjson.JSONObject;
import com.se231.onlineedu.exception.FileFormatNotSupportException;
import com.se231.onlineedu.exception.FileSizeExceededException;
import com.se231.onlineedu.exception.ValidationException;
import com.se231.onlineedu.message.response.PersonalInfo;
import com.se231.onlineedu.message.response.UserAvatar;
import com.se231.onlineedu.model.Course;
import com.se231.onlineedu.model.User;
import com.se231.onlineedu.model.VerificationToken;
import com.se231.onlineedu.security.services.UserPrinciple;
import com.se231.onlineedu.service.EmailSenderService;
import com.se231.onlineedu.service.UserService;
import com.se231.onlineedu.service.VerificationTokenService;
import com.se231.onlineedu.util.FileCheckUtil;
import com.se231.onlineedu.util.SaveFileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * User Controller Class
 * <p>
 * controller used to handle request related to user.
 *
 * @author Zhe Li
 * @date 2019/07/08
 */
@RestController
@RequestMapping("/api/users")
@Api(tags = "用户信息控制类", value = "用户信息相关的api")
public class UserController {
    private static int limit = 5120000;

    @Autowired
    UserService userService;

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    VerificationTokenService verificationTokenService;

    @ApiOperation(value = "已登录用户查询个人信息的用户名", notes = "已登录用户查询个人信息的用户名", httpMethod = "GET")
    @GetMapping("/{id}/username")
    public String getPersonalUsername(@PathVariable Long id) {
        return userService.getUserInfo(id).getUsername();
    }


    @ApiOperation(value = "已登录用户查询个人信息", notes = "已登录用户查询个人信息", httpMethod = "GET")
    @GetMapping("/info")
    public User getPersonalInfo(@AuthenticationPrincipal UserPrinciple userPrinciple) {
        return userService.getUserInfo(userPrinciple.getId());
    }

    @ApiOperation(value = "已登录用户查询个人信息的已教课程", notes = "已登录用户查询个人信息", httpMethod = "GET")
    @GetMapping("/info/courses/teach")
    public List<Course> getPersonalInfoTeach(@AuthenticationPrincipal UserPrinciple userPrinciple) {
        User user = userService.getUserInfo(userPrinciple.getId());
        return user.getTeachCourses();
    }

    @ApiOperation(value = "已登录用户查询个人信息的TA课程", notes = "已登录用户查询个人信息", httpMethod = "GET")
    @GetMapping("/info/courses/assist")
    public List<Course> getPersonalInfoAssist(@AuthenticationPrincipal UserPrinciple userPrinciple) {
        User user = userService.getUserInfo(userPrinciple.getId());
        return user.getAssistCourses();
    }

    @ApiOperation(value = "已登录用户查询个人信息的学习课程", notes = "已登录用户查询个人信息", httpMethod = "GET")
    @GetMapping("/info/courses/learn")
    public List<Course> getPersonalInfoLearn(@AuthenticationPrincipal UserPrinciple userPrinciple) {
        User user = userService.getUserInfo(userPrinciple.getId());
        return user.getLearnCourses();
    }


    @ApiOperation(value = "用户修改自己的个人信息", httpMethod = "POST")
    @PostMapping("/info/modify")
    public User modifyPersonalInfo(@AuthenticationPrincipal UserPrinciple userPrinciple,
                                   @Valid @RequestBody PersonalInfo personalInfo) {
        return userService.manageUserInfo(userPrinciple.getId(), personalInfo);
    }


    @ApiOperation(value = "管理员修改用户的个人信息", httpMethod = "POST")
    @ApiImplicitParam(value = "修改的用户的id", name = "id", type = "path", dataTypeClass = Long.class)
    @PostMapping("{id}/info/modify")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public User managePersonalInfo(@Valid @RequestBody PersonalInfo personalInfo,
                                   @PathVariable("id") Long id){
        return userService.manageUserInfo(id, personalInfo);
    }

    @ApiOperation(value = "管理员获取全部用户的信息", httpMethod = "GET")
    @GetMapping("/info/all")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public List<User> getAllUser() {
        return userService.getAllUser();
    }


    @ApiOperation(value = "查询是否有重复的用户名", httpMethod = "GET")
    @ApiResponse(code = 200, response = Boolean.class, message = "真则为存在重复用户名")
    @ApiImplicitParam(name = "username", value = "待查的用户名",
            required = true, dataTypeClass = String.class, type = "param")
    @GetMapping("/checkSame/username")
    public Boolean checkSameUsername(@RequestParam("username") String username) {
        return userService.checkSameUsername(username);
    }


    @ApiOperation(value = "查询是否存在重复的邮箱地址", httpMethod = "GET")
    @ApiResponse(code = 200, response = Boolean.class, message = "真则为存在重复邮箱地址")
    @ApiImplicitParam(value = "待查询的邮箱地址", name = "email",
            required = true, dataTypeClass = String.class, type = "param")
    @GetMapping("/checkSame/email")
    public Boolean checkSameEmail(@RequestParam("email") String email) {
        return userService.checkSameEmail(email);
    }

    @ApiOperation(value = "查询是否存在重复的电话号码", httpMethod = "GET")
    @ApiResponse(code = 200, response = Boolean.class, message = "真则为存在重复电话号码")
    @ApiImplicitParam(value = "待查询的电话号码", name = "tel",
            required = true, dataTypeClass = String.class)
    @GetMapping("/checkSame/tel")
    public Boolean checkSameTel(@RequestParam("tel") String tel) {
        return userService.checkSameTel(tel);
    }


    @ApiOperation(value = "用户可以个人的头像", httpMethod = "POST")
    @ApiImplicitParam(name = "id", value = "上传的用户id", type = "path")
    @PostMapping("/{id}/avatar")
    @PreAuthorize("#id == authentication.principal.id")
    public String patchAvatar(@PathVariable Long id, @RequestParam(value = "avatar") MultipartFile multipartFile) throws IOException {
        if (FileCheckUtil.checkImageSizeExceed(multipartFile, limit)) {
            throw new FileSizeExceededException("文件请不要超过5M");
        }
        String suffix = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf("."));
        if (FileCheckUtil.checkImageTypeWrong(suffix)) {
            throw new FileFormatNotSupportException("文件格式不支持");
        }
        return userService.updateUserAvatar(SaveFileUtil.saveFile(multipartFile, suffix), id);
    }


    @ApiOperation(value = "用户修改个人的密码", httpMethod = "PATCH")
    @PatchMapping("/{id}/password")
    @PreAuthorize("#id == authentication.principal.id")
    public String patchPassword(@PathVariable Long id, HttpSession httpSession, @RequestBody JSONObject passwordJSON){
        String password = (String) passwordJSON.get("password");
        if (userService.checkIfSameAsOldPassword(id, password)) {
            throw new ValidationException("新密码不可与旧密码相同");
        }
        httpSession.setAttribute("password", passwordJSON.get("password"));
        return sendEmail(httpSession, id);
    }

    @ApiOperation(value = "用户密码确认修改", httpMethod = "GET")
    @GetMapping("/{id}/password/confirm")
    @PreAuthorize("#id == authentication.principal.id")
    public String patchPasswordConfirm(@PathVariable Long id, HttpSession httpSession, @RequestParam("verificationToken") String token) {
        VerificationToken verificationToken = (VerificationToken) httpSession.getAttribute("token");
        verificationTokenService.verify(verificationToken, token);
        String password = (String) httpSession.getAttribute("password");
        userService.updateUserPasswordConfirm(id, password);
        return "修改成功";

    }


    @ApiOperation(value = "用户修改个人的邮箱", httpMethod = "PATCH")
    @PatchMapping("/{id}/email")
    @PreAuthorize("#id == authentication.principal.id")
    public String patchEmail(@PathVariable Long id, HttpSession httpSession, @RequestBody JSONObject emailJSON) {
        String email = (String) emailJSON.get("email");
        if (userService.checkIfSameAsOldEmail(id, email)) {
            throw new ValidationException("新邮箱不可与旧邮箱相同");
        }
        httpSession.setAttribute("email", email);
        return sendEmail(httpSession, id);
    }

    @ApiOperation(value = "用户邮箱确认修改", httpMethod = "GET")
    @GetMapping("/{id}/email/confirm")
    @PreAuthorize("#id == authentication.principal.id")
    public String patchEmailConfirm(@PathVariable Long id, HttpSession httpSession, @RequestParam("verificationToken") String token) {
        VerificationToken verificationToken = (VerificationToken) httpSession.getAttribute("token");
        verificationTokenService.verify(verificationToken, token);
        String email = (String) httpSession.getAttribute("email");
        userService.updateUserEmailConfirm(id, email);
        return "修改成功";
    }

    @ApiOperation(value = "管理员或老师批量导入学生信息", httpMethod = "POST")
    @ApiImplicitParam(value = "上传的excel表格必须为xls或xlsx格式，注意第一行为表头，从第二行开始为正式数据," +
            "依次为username,password,email,tel,university,major,sno,grade,real name,sex", type = "FormData", name = "excel")
    @PostMapping("/bulkImport")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public String bulkImportUser(@RequestParam("excel") MultipartFile excel) throws IOException {
        return userService.bulkImportUser(excel);
    }


    private String sendEmail(HttpSession httpSession, Long id)  {
        VerificationToken verificationToken = verificationTokenService.generateToken();
        httpSession.setAttribute("token", verificationToken);
        emailSenderService.sendVerificationEmail(userService.getUserInfo(id).getEmail(), verificationToken);
        return "已发送验证码";
    }

    @ApiOperation("获取用户头像")
    @GetMapping("/{userId}/avatar")
    public UserAvatar getUserAvatar(@PathVariable("userId")Long userId){
        return userService.getUserAvatar(userId);

    }

}
