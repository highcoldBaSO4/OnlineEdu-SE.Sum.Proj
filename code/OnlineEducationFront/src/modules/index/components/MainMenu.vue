<template>
    <el-menu mode="horizontal"
             background-color="#F0F6FF"
             active-text-color="#116ECD"
             text-color="#000000"
             router>
        <div class="head-logo"><img alt="fuck" src="../../../assets/mainlogo.png" style="width: 160px;"></div>
        <el-menu-item index="/home">
            <i class="iconfont el-icon-my-home"></i>
            <span class="menu-text"> 首页</span>
        </el-menu-item>
        <el-menu-item index="/viewcourses">
            <i class="iconfont el-icon-my-list"></i>
            <span class="menu-text"> 课程广场</span></el-menu-item>
        <el-menu-item index="/user">
            <i class="el-icon-user"></i>
            <span class="menu-text"> 个人中心</span>
        </el-menu-item>
        <div class="head-right">
            <div class="head-right head-user head-user-auto">
                <HeadSideUser></HeadSideUser>
            </div>
            <div class="head-right head-search">
                <SearchInput></SearchInput>
            </div>
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
        margin-right: 260px;
        border-style: none;
        border-color: white;
    }

    @media screen and (max-width: 1490px) {
        .head-logo {
            margin-right: 200px;
        }
    }

    @media screen and (max-width: 1430px) {
        .head-logo {
            margin-right: 100px;
        }
    }

    @media screen and (max-width: 1330px) {
        .head-logo {
            margin-right: 0px;
        }
    }

    @media screen and (max-width: 1230px) {
        .head-logo {
            margin-left: 0px;
        }

        .head-user-auto {
            margin-right: 0;
        }
    }

    @media screen and (max-width: 1120px) {
        .menu-text {
            display: none;
        }
    }

    @media screen and (max-width: 960px) {
        .head-search {
            display: none;
        }
    }

    .head-right {
        float: right;
        margin-top: 5px;
    }

    .head-user {
        float: right;
        margin-right: 100px;
    }

    .head-search {
        margin-right: 50px;
    }

</style>
