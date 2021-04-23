<template>
    <el-container>
        <el-aside>
            <CourseHead></CourseHead>
            <UserCourseMenu></UserCourseMenu>
        </el-aside>
        <el-main>
            <router-view></router-view>
        </el-main>
    </el-container>
</template>

<script>
    import CourseHead from "../components/CourseHead";
    import UserCourseMenu from "../components/UserCourseMenu";
    export default {
        name: "Course",
        components: {UserCourseMenu, CourseHead}
    }
</script>

<style scoped>

</style>