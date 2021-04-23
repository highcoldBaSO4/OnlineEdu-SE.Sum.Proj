<template>
    <el-menu mode="horizontal" router>

        <div class="head-logo head-div"><img src="../assets/mainlogo.png" style="width: 160px;"></div>
        <el-menu-item index="/home">首页</el-menu-item>
        <el-menu-item index="/viewcourses">课程</el-menu-item>
        <el-menu-item index="/user">个人中心</el-menu-item>
        <div class="head-right head-div">
            <div class="head-right head-user"><HeadSideUser></HeadSideUser></div>
            <div class="head-right head-search"><SearchInput></SearchInput></div>
        </div>
    </el-menu>
</template>

<script>
    import SearchInput from "./SearchInput";
    import HeadSideUser from "./HeadSideUser";
    export default {
        name: "MainMenu",
        components: {HeadSideUser, SearchInput}
    }
</script>

<style scoped>
    .head-logo {
        float: left;
        margin-left: 90px;
        margin-right: 350px;
        border-style: none;
        border-color: white;
    }

    .head-right {
        float: right;
        margin-top: 5px;
    }

    .head-user {
        margin-right: 90px;
    }

    .head-search {
        margin-right: 50px;
    }

    .head-div:active {
        border-style: none;
    }
</style>