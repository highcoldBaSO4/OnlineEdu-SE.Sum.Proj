<template>
    <div>
        <div class="login-square float-right">
            <div class="login-form">
                <h3>登录</h3>
                <el-form :model="loginInfo"
                         :rules="rules"
                         ref="loginInfo"
                >
                    <el-form-item prop="username">
                        <el-input
                                placeholder="用户名"
                                suffix-icon="el-icon-user"
                                id="userId"
                                v-model="loginInfo.username"
                        ></el-input>
                    </el-form-item>
                    <el-form-item prop="password">
                        <el-input
                                type="password"
                                placeholder="密码"
                                suffix-icon="el-icon-lock"
                                v-model="loginInfo.password"
                        ></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" size="medium" :loading="loginLoading" class="login-text" @click="login">
                            登录
                        </el-button>
                    </el-form-item>
                    <el-form-item>
                        <div class="float-left">
                            <el-link :underline="false">忘记密码</el-link>
                        </div>
                        <div class="float-right">
                            <el-link :underline="false" href="/#/register">注册新用户</el-link>
                        </div>
                        <div class="float-clear"></div>
                    </el-form-item>
                </el-form>
            </div>
        </div>
        <div style="clear:both"></div>
    </div>
</template>

<script>
    import FormRules from "../rules.js"

    export default {
        name: "Login",
        data() {
            return {
                loginInfo: {
                    username: "",
                    password: ""
                },
                rules: {
                    username: [FormRules.usernameRule[0], FormRules.usernameRule[1]],
                    password: FormRules.passwordRule
                },
                loginLoading: false
            }
        },
        methods: {
            login: function () {
                this.$refs["loginInfo"].validate((valid) => {
                    if (valid) {
                        this.loginLoading = true;
                        this.$http.request({
                            url: "/api/auth/signin",
                            method: "post",
                            data: {
                                username: this.loginInfo.username,
                                password: this.loginInfo.password
                            }
                        }).then((response) => {
                            console.log(response.data);
                            let getToken = response.data.accessToken;
                            this.$store.commit("loginSet", {
                                username: this.loginInfo.username,
                                accessToken: getToken
                            });
                            this.$root.success("登录成功");
                            //console.log(state);
                            this.$http.request({
                                url: "/api/users/info",
                                method: 'get',
                                headers: {
                                    "Authorization": "Bearer " + getToken
                                }
                            }).then((infoResponse) => {
                                this.loginLoading = false;
                                this.$store.commit("infoSet", infoResponse.data);
                                //console.log(state);
                                if (infoResponse.data.roles[0].role === "ROLE_ADMIN") {
                                    localStorage.setItem("managerToken", getToken);
                                    // window.location = "/manager";
                                    window.location.href = '/manage.html#/'
                                }
                                this.$router.push('/user');
                            }).catch((error) => {
                                this.loginLoading = false;
                                console.log(error.response);
                                if (error.response.data.status === 401) {
                                    this.$root.error("用户登录出错，请重新登录");
                                }
                                else {
                                    this.$root.error(error);
                                }
                            });
                            //dispatch("loadUserInfo");
                        }).catch((error) => {
                            this.loginLoading = false;
                            console.log(error.response);
                            if (error.response.data.status === 401) {
                                this.$root.error("用户名或密码错误");
                            }
                            else {
                                this.$root.error(error);
                            }
                        });
                        /*this.$store.dispatch('login', this.loginInfo).then(() => {
                            this.$router.push('/user')
                        });*/
                    }
                })
            }
        }
    }
</script>

<style scoped>
    .login-square {
        width: 350px;
        margin-top: 100px;
        margin-bottom: 100px;
        margin-right: 30px;
        border-style: solid;
        border-radius: 40px;
    }

    .login-form {
        margin: 40px;
    }

    .login-text {
        width: 270px;
    }
</style>
