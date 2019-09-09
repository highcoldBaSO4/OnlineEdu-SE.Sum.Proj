package com.se231.onlineedu.message.request;

import java.util.List;

import javax.validation.constraints.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * SignUpForm Class
 *
 * Sign up request form
 *
 * @author Yuxuan Liu
 *
 * @date 2019/07/01
 */
@ApiModel(value = "注册表格",description = "用于用户提交注册信息")
public class SignUpForm {
    @ApiModelProperty(value = "用户名",required = true)
    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @ApiModelProperty(value = "密码",required = true)
    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    @ApiModelProperty(value = "邮箱",required = true,notes = "有格式验证且不能重复")
    @Email
    private String email;

    @ApiModelProperty(value = "手机号码",required = true,
            notes = "必须以13,14,15,18,17开头且为11位",dataType = "String")
    @Pattern(regexp = "0?(13|14|15|18|17)[0-9]{9}")
    private String tel;

    @ApiModelProperty(value = "大学")
    private String university;

    @ApiModelProperty(value = "专业")
    private String major;

    @ApiModelProperty(value = "年级",dataType = "int",
            notes = "1代表大一，2代表大二，5代表研一，以此类推")
    private int grade;

    @ApiModelProperty(value = "学号",dataType = "String")
    private String sno;

    @ApiModelProperty(value = "真名")
    private String realName;

    @ApiModelProperty(value = "性别",dataType = "String",notes = "男/女")
    private String sex;

    public SignUpForm(@NotBlank @Size(min = 3, max = 50) String username, List<String> roles, @NotBlank @Size(min = 6, max = 40) String password) {
        this.username = username;
        this.password = password;
    }

    public SignUpForm() {
    }

    public SignUpForm(@NotBlank @Size(min = 3, max = 50) String username, @NotBlank @Size(min = 6, max = 40) String password, @Email String email, @Pattern(regexp = "0?(13|14|15|18|17)[0-9]{9}")
            String tel, String university, String major, int grade, String sno, String realName, String sex) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.tel = tel;
        this.university = university;
        this.major = major;
        this.grade = grade;
        this.sno = sno;
        this.realName = realName;
        this.sex = sex;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}