<template>
    <div>
        <div class="step-line">
            <el-steps :active="currentStep">
                <el-step title="账号信息" icon="el-icon-user-solid"></el-step>
                <el-step title="详细信息" icon="el-icon-s-order"></el-step>
                <el-step title="邮箱验证" icon="el-icon-message"></el-step>
                <el-step title="注册完成" icon="el-icon-check"></el-step>
            </el-steps>
        </div>
        <div v-if="currentStep === 0">
            <div class="step-layout">
                <el-form
                        label-position="left"
                        label-width="80px"
                        :model="registerUser"
                        :rules="step1Rules"
                        ref="step1Form"
                >
                    <el-form-item label="用户名" prop="username">
                        <el-input
                                type="text"
                                suffix-icon="el-icon-user"
                                placeholder="请在此创建你的用户名"
                                v-model="registerUser.username"
                        ></el-input>
                    </el-form-item>
                    <el-form-item label="密码" prop="password">
                        <el-input
                                type="password"
                                suffix-icon="el-icon-lock"
                                placeholder="请在此创建密码"
                                v-model="registerUser.password"
                        ></el-input>
                    </el-form-item>
                    <el-form-item label="确认密码" prop="confirmPass">
                        <el-input
                                type="password"
                                suffix-icon="el-icon-lock"
                                placeholder="请重新输入你创建的密码"
                                v-model="registerUser.confirmPass"
                        ></el-input>
                    </el-form-item>
                    <el-form-item label="邮箱" prop="email">
                        <el-input
                                type="text"
                                suffix-icon="el-icon-message"
                                placeholder="请输入你的邮箱"
                                v-model="registerUser.email"
                        ></el-input>
                    </el-form-item>
                    <el-form-item label="手机号" prop="tel">
                        <el-input type="text"
                                  suffix-icon="el-icon-phone-outline"
                                  placeholder="请输入你的手机号码"
                                  v-model="registerUser.tel"
                        ></el-input>
                    </el-form-item>
                    <el-form-item>
                        <div class="center-layout next-button">
                            <el-button type="primary" @click="submitStep1" :loading="step1Loading">下一步</el-button>
                        </div>
                    </el-form-item>
                </el-form>
            </div>
        </div>
        <div v-else-if="currentStep === 1">
            <div class="step-layout">
                <UserInfoManage
                        :userdata="registerDetailInfo"
                        type="register"
                        @submit-info="submitStep3"
                        @on-back="lastStep"
                        :loading="step2Loading"
                ></UserInfoManage>
            </div>
        </div>
        <div v-else-if="currentStep === 2">
            <div class="step-layout">
                <EmailConfirm
                        confirm-type="register"
                        ref="emailConfirm"
                        @confirm-pass="finishRegister"
                        @resend-request="resendRequest"
                        @confirm-fail="failConfirm"
                ></EmailConfirm>
                <div class="center-layout button-group-size">
                    <el-button type="primary" @click="lastStep">返回</el-button>
                    <el-button type="primary" @click="submitStep2" :loading="step3Loading">提交</el-button>
                </div>
            </div>
        </div>
        <div v-else-if="currentStep === 3">
            <div class="step-layout">
                <h3>恭喜你，注册成功！请点击下方链接进行登录</h3>
            </div>
        </div>
        <div class="center-layout return-link">
            <div class="return-offset">
                <el-link href="/#/login">已有账号？点此返回登录</el-link>
            </div>
        </div>
    </div>
</template>

<script>
    import UserInfoManage from "../components/UserInfoManage";
    import FormRules from "../rules.js";
    import EmailConfirm from "../components/EmailConfirm";

    export default {
        name: "Register",
        components: {EmailConfirm, UserInfoManage},
        data() {
            var checkConfirmPass = (rule, value, callback) => {
                if (value !== this.registerUser.password) {
                    callback(new Error("两次输入的密码不符"));
                }
                else {
                    callback();
                }
            };
            return {
                currentStep: 0,
                resendFlag: false,
                countDown: 60,
                registerUser: {
                    username: "",
                    password: "",
                    confirmPass: "",
                    email: "",
                    tel: ""
                },
                step1Rules: {
                    username: FormRules.usernameRule,
                    password: FormRules.passwordRule,
                    confirmPass: [
                        {required: true, message: "确认密码不能为空", trigger: "change"},
                        {validator: checkConfirmPass, trigger: "change"}
                    ],
                    email: FormRules.emailRule,
                    tel: FormRules.telRule,
                },
                totalRegisterInfo: {
                    username: "",
                    password: "",
                    email: "",
                    tel: "",
                    realName: "",
                    sex: "",
                    university: "",
                    sno: "",
                    major: "",
                    grade: 1,
                },
                registerDetailInfo: {
                    realName: "",
                    tel: "",
                    sex: "",
                    university: "",
                    sno: "",
                    major: "",
                    grade: 1,
                },
                step1Loading: false,
                step2Loading: false,
                step3Loading: false
            }
        },
        methods: {
            nextStep: function() {
                this.currentStep++;
            },
            lastStep: function() {
                this.currentStep--;
            },
            submitStep1: function () {
                this.step1Loading = true;
                this.$refs["step1Form"].validate((valid) => {
                    if (valid) {
                        this.step1Loading = false;
                        this.nextStep();
                    }
                })
            },
            submitStep2: function () {
                this.step3Loading = true;
                this.$refs['emailConfirm'].sendConfirmCode();
                //this.nextStep();
            },
            submitStep3: function(detailInfo) {
                let totalRegisterInfo = {
                    username: this.registerUser.username,
                    password: this.registerUser.password,
                    email: this.registerUser.email,
                    tel: this.registerUser.tel,
                    realName: detailInfo.realName,
                    sex: detailInfo.sex,
                    university: detailInfo.university,
                    sno: detailInfo.sno,
                    major: detailInfo.major,
                    grade: detailInfo.grade
                };
                this.totalRegisterInfo = totalRegisterInfo;
                console.log(totalRegisterInfo);
                this.step2Loading = true;
                this.$http.request({
                    url: "/api/auth/signup",
                    method: "post",
                    data: totalRegisterInfo,
                    withCredentials: true
                }).then((response) => {
                    this.step2Loading = false;
                    console.log(response);
                    this.nextStep();
                }).catch((error) => {
                    this.step2Loading = false;
                    console.log(error.response);
                    if (error.response.data === "Fail -> Email Address is already taken!") {
                        this.$root.error("邮箱已被使用，请更换邮箱");
                    }
                })
            },
            finishRegister: function () {
                this.step3Loading = false;
                this.$root.success("注册成功！");
                this.nextStep();
            },
            resendRequest: function () {
                this.$http.request({
                    url: "/api/auth/signup",
                    method: "post",
                    data: this.totalRegisterInfo,
                    withCredentials: true
                }).then((response) => {
                    console.log(response);
                }).catch((error) => {
                    console.log(error.response);
                    if (error.response.data === "Fail -> Email Address is already taken!") {
                        this.$root.error("邮箱已被使用，请更换邮箱");
                    }
                })
            },
            failConfirm: function () {
                this.step3Loading = false;
            }
        }
    }
</script>

<style scoped>
    .step-line {
        width: 1000px;
        margin-left: auto;
        margin-right: auto;
    }

    .step-layout {
        width: 450px;
        margin-left: auto;
        margin-right: auto;
        margin-top: 50px;
    }

    .button-group-size {
        margin-top: 40px;
        width: 190px;
        margin-bottom: 20px;
    }

    .next-button {
        width: 100px;
    }

    .return-link {
        width: 300px;
        text-align: center;

    }

    /*.return-offset {
        margin-left:30px;
    }*/
</style>
