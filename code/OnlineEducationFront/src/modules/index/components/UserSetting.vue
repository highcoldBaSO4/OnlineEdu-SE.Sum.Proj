<template>
    <div class="content-body-layout">
        <el-tabs type="border-card">
            <el-tab-pane>
                <span slot="label">用户设置</span>
                <UserCoreInfoManage></UserCoreInfoManage>
            </el-tab-pane>
            <el-tab-pane>
                <span slot="label">基本信息</span>
                <UserInfoManage
                        :userdata="userDetailInfo"
                        @submit-info="uploadDetailInfo"
                        ref="userInfoManage"
                        :loading="loading"
                ></UserInfoManage>
            </el-tab-pane>
        </el-tabs>
    </div>
</template>

<script>
    import UserInfoManage from "./UserInfoManage";
    import UserCoreInfoManage from "./UserCoreInfoManage";
    export default {
        name: "UserSetting",
        components: {UserCoreInfoManage, UserInfoManage},
        data() {
            return {
                loading: false
            }
        },
        methods:{
            uploadDetailInfo: function (userDetailInfo) {
                this.loading = true;
                this.$http.request({
                    url: "/api/users/info/modify",
                    method: "post",
                    data: userDetailInfo,
                    headers: this.requestHead
                }).then(() => {
                    alert("修改成功！");
                    this.updateUserInfo();
                    this.loading = false;
                    //this.$refs['userInfoManage'].uploadDetailInfo(this.userDetailInfo);
                }).catch((error) => {
                    console.log(error.response);
                    alert("出错啦");
                    this.loading = false;
                })
            },
            updateUserInfo: function () {
                this.$http.request({
                    url: "/api/users/info",
                    method: 'get',
                    headers: {
                        "Authorization": "Bearer " + this.$store.state.user.accessToken
                    }
                }).then((infoResponse) => {
                    this.$store.commit("infoSet", infoResponse.data);
                }).catch((error) => {
                    console.log(error.response);
                    if (error.response.data.status === 401) {
                        alert("获取用户信息出错");
                    }
                    else {
                        alert(error);
                    }
                });
            }
        },
        computed:{
            userDetailInfo: function () {
                let temp = this.$store.state.user.userInfo;
                return {
                    realName: temp.realName,
                    sex: temp.sex,
                    tel: temp.tel,
                    university: temp.university,
                    sno: temp.sno,
                    major: temp.major,
                    grade: temp.grade,
                    email: temp.email
                }
            },
            requestHead: function () {
                return this.$store.getters.authRequestHead;
            }
        }
    }
</script>

<style scoped>
    .setting-layout {
        width: 1000px;
        margin-left: 25px;
        margin-top: 30px;
    }
</style>
