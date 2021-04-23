<template>
    <div>
        <el-row class="content-row">
            <el-col :span="4" :push="1">修改头像：</el-col>
            <el-col :span="19">
                <UserAvatarUpload></UserAvatarUpload>
            </el-col>
        </el-row>
        <el-row class="content-row">
            <el-col :span="4" :push="1">修改密码：</el-col>
            <el-col :span="19">
                <UserPasswordChange></UserPasswordChange>
            </el-col>
        </el-row>
        <el-row class="content-row">
            <el-col :span="4" :push="1">修改邮箱：</el-col>
            <el-col :span="10">
                <UserEmailChange :old-email="oldEmail"></UserEmailChange>
            </el-col>
        </el-row>
    </div>
</template>

<script>
    import UserAvatarUpload from "./UserAvatarUpload";
    import UserPasswordChange from "./UserPasswordChange";
    import UserEmailChange from "./UserEmailChange";
    export default {
        name: "UserCoreInfoManage",
        components: {UserEmailChange, UserPasswordChange, UserAvatarUpload},
        computed: {
            oldEmail: function () {
                return this.$store.state.user.userInfo.email;
            }
        }
    }
</script>

<style scoped>
    .content-row {
        margin-bottom: 30px;
    }
</style>
