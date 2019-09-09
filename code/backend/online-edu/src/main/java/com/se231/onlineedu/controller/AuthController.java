package com.se231.onlineedu.controller;

import com.se231.onlineedu.exception.VerificationTokenExpiredException;
import com.se231.onlineedu.exception.VerificationTokenWrongException;
import com.se231.onlineedu.message.request.LoginForm;
import com.se231.onlineedu.message.request.SignUpForm;
import com.se231.onlineedu.message.response.JwtResponse;
import com.se231.onlineedu.model.User;
import com.se231.onlineedu.model.VerificationToken;
import com.se231.onlineedu.service.AuthService;
import com.se231.onlineedu.service.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Calendar;

/**
 * AuthController Class
 *
 * log in and sign up controller
 *
 *
 * @author Yuxuan Liu
 *
 * @date 2019/07/01
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@Api(tags = "与用户权限有关的控制类")
public class AuthController {

    @Autowired
    AuthService authService;

    @Autowired
    UserService userService;


    @ApiOperation(value = "用户登录", notes = "用户提供用户名和密码用于登录")
    @ApiResponses({
            @ApiResponse(code = 401, message = "Error -> Unauthorized"),
            @ApiResponse(code = 200, message = "sign in successfully")
    })
    @PostMapping("/signin")
    @ResponseBody
    public JwtResponse authenticateUser(@Valid @RequestBody LoginForm loginRequest) {
        return authService.userSignIn(loginRequest.getUsername(),loginRequest.getPassword());
    }


    @ApiOperation(value = "用户注册",notes = "用户填写个人信息注册")
    @ApiResponses({
            @ApiResponse(code = 200, message = "sign up successfully"),
            @ApiResponse(code = 400, message = "sign up unsuccessfully," +
                    "mainly because of existing same username/email/tel,or invalid param"),
    })
    @PostMapping("/signup")
    public User registerUser(HttpSession httpSession, @Valid @RequestBody SignUpForm signUpRequest) {
        return authService.userSignUp(signUpRequest, httpSession);
    }

    @ApiOperation(value = "管理员任命教师",notes = "管理员任命用户为教师")
    @ApiImplicitParam(value = "被任命用户的id",dataType = "Long",
            paramType = "path",required = true)
    @PostMapping("/{id}/teachingAdmin")
    @PreAuthorize("hasAnyRole('ADMIN','SPUER_ADMIN')")
    public String addTeachingAdmin(@PathVariable(name = "id")Long id){
        return authService.addTeachingAdmin(id);
    }

    @ApiOperation(value = "用户注册",notes = "用户注册激活")
    @ApiResponses({
            @ApiResponse(code = 200, message = "已激活"),
            @ApiResponse(code = 400, message = "验证码已失效"),
    })
    @GetMapping("/registrationConfirm")
    public User confirmRegistration(HttpSession httpSession, @RequestParam("verificationToken") String token){
        VerificationToken verificationToken = (VerificationToken)httpSession.getAttribute("token");
        if(!verificationToken.getToken().equals(token)){
            throw new VerificationTokenWrongException();
        }
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            throw new VerificationTokenExpiredException();
        }

        User user = (User)httpSession.getAttribute("user");
        return authService.saveRegisteredUser(user);
    }
}