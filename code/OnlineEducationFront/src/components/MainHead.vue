<template>
    <div>
        <el-row align="bottom" justify="end" :gutter="20">
            <el-col :span="6" :offset="1">
                <img src="../assets/mainlogo.png" style="width: 180px;">
            </el-col>
            <el-col :span="8">
                <MainMenu></MainMenu>
            </el-col>
            <el-col :span="4">
                <div style="margin-top: 13px"><SearchInput></SearchInput></div>
            </el-col>
            <el-col :span="3" :push="1">
                <div style="margin-top: 8px;"><HeadSideUser></HeadSideUser></div>
            </el-col>
        </el-row>
    </div>
</template>

<script>
    import MainMenu from "./MainMenu";
    import SearchInput from "./SearchInput";
    import HeadSideUser from "./HeadSideUser";
    export default {
        name: "MainHead",
        components: {HeadSideUser, SearchInput, MainMenu}
    }
</script>

<style scoped>

</style>