<template>
    <div style="width: 103px">
        <div v-if="loginStatus">
            <el-avatar :src="avatarUrl" fit="fill"></el-avatar>{{ username }}
        </div>
        <div v-else>
            <el-avatar icon="el-icon-user-solid"></el-avatar>
            <el-link type="primary" href="/#/login">登录</el-link>/<el-link type="primary" href="/#/register">注册</el-link>
        </div>
    </div>
</template>

<script>
    export default {
        name: "HeadSideUser",
        data() {
            return {
            }
        },
        computed: {
            username() {
                return this.$store.state.user.username;
            },
            loginStatus() {
                return this.$store.state.user.loginStatus;
            },
            avatarUrl() {
                console.log(this.$store.getters.userAvatarUrl);
                return this.$store.getters.userAvatarUrl;
            }
        }
    }
</script>

<style scoped>

</style>
