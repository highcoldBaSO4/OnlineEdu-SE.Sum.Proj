<template>
    <div>
        <el-form>
            <el-form-item>
                <h3>系统已向你的邮箱发送了一封邮件，请从中获取验证码</h3>
            </el-form-item>
            <el-form-item>
                <el-input type="text" v-model="confirmCode" placeholder="请输入验证码">
                    <el-button
                            v-if="resendFlag"
                            slot="append"
                            @click="resendConfirmRequest"
                    >重新发送</el-button>
                    <el-button v-else slot="append" disabled>{{ countDown }}s后重新发送</el-button>
                </el-input>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
    export default {
        name: "EmailConfirm",
        props: {
            confirmType: {
                type: String,
                default: "password",
                validator: function (value) {
                    return [
                        "password",
                        "email",
                        "register"
                    ].indexOf(value) !== -1
                }
            }
        },
        data() {
            return {
                countDown: 60,
                countDownId: 0,
                confirmCode: "",
                resendFlag: false,
            }
        },
        methods: {
            countTime: function () {
                this.resendFlag = false;
                this.countDownId = setInterval(this.countDownAction,1000);
            },
            countDownAction: function() {
                this.countDown--;
                if (this.countDown === 0) {
                    this.countDown = 60;
                    this.resendFlag = true;
                    clearInterval(this.countDownId);
                }
            },
            sendConfirmRequest: function () {
                this.countTime();
            },
            resendConfirmRequest: function () {
                this.$emit("resend-request");
                this.countTime();
            },
            sendConfirmCode: function () {
                console.log(this.confirmCode);
                this.$http.request(this.requestConfig).then(() => {
                    this.$emit("confirm-pass");
                }).catch((error) => {
                    this.$emit("confirm-fail");
                    console.log(error.response);
                    if (error.response.status === 400) {
                        this.$root.error("验证码无效");
                    }
                    else {
                        this.$root.error("验证失败");
                    }
                })
            },
            clear: function () {
                clearInterval(this.countDownId);
                this.countDown = 60;
                this.countDownId = 0;
                this.confirmCode = "";
                this.resendFlag = false;
            }
        },
        mounted() {
            this.sendConfirmRequest();
        },
        computed: {
            requestConfig: function () {
                if (this.confirmType === "register") return {
                    url: "/api/auth/registrationConfirm",
                    method: "get",
                    params: {
                        verificationToken: this.confirmCode
                    },
                    withCredentials: true
                };
                else {
                    let id = this.$store.state.user.userInfo.id;
                    return {
                        url: "/api/users/" + id + "/" + this.confirmType + "/confirm",
                        method: "get",
                        params: {
                            verificationToken: this.confirmCode
                        },
                        headers: this.$store.getters.authRequestHead,
                        withCredentials: true
                    };
                }
            }
        }
    }
</script>

<style scoped>

</style>
