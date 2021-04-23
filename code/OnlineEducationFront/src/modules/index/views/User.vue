<template>
    <el-container>
        <el-aside>
            <UserHead></UserHead>
            <UserMenu></UserMenu>
        </el-aside>
        <el-main>
            <router-view></router-view>
        </el-main>
    </el-container>
</template>

<script>
    import UserHead from "../components/UserHead";
    import UserMenu from "../components/UserMenu";
    export default {
        name: "User",
        components: {UserMenu, UserHead},
        created() {
            if (!this.$store.state.user.loginStatus) {
                this.$router.push("/login");
            }
        }
    }
</script>

<style scoped>

</style>