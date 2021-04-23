package com.se231.onlineedu.message.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * LoginForm Class
 *
 * Login Request Form
 *
 * @author Yuxuan Liu
 *
 * @date 2019/07/01
 */
@ApiModel(value = "用户登录的表格",description = "用户输入用户名和密码登录")
public class LoginForm {
    @ApiModelProperty(value = "用户名",required = true)
    @NotBlank
    @Size(min=3, max = 60)
    private String username;

    @ApiModelProperty(value = "密码",required = true)
    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

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
}