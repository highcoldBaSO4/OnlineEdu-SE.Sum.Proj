<template>
    <el-menu class="course-menu" router>
        <el-menu-item index="/course/student/info">
            <i class="iconfont el-icon-my-info"></i>
            课程信息
        </el-menu-item>
        <el-menu-item index="/course/student/chapters">
            <i class="iconfont el-icon-my-nav"></i>
            章节导航
        </el-menu-item>
        <el-menu-item index="/course/student/homework">
            <i class="iconfont el-icon-my-homework"></i>
            课程作业
        </el-menu-item>
        <!--<el-menu-item index="/course/student/resources">-->
            <!--<i class="iconfont el-icon-my-resource"></i>-->
            <!--课程资源-->
        <!--</el-menu-item>-->
        <el-menu-item index="/course/student/forum">
            <i class="iconfont el-icon-my-forum"></i>
            课程论坛
        </el-menu-item>
        <el-menu-item index="/course/student/report">
            <i class="iconfont el-icon-my-stat"></i>
            学习报告
        </el-menu-item>
    </el-menu>
</template>

<script>
    export default {
        name: "UserCourseMenu"
    }
</script>

<style scoped>
    .course-menu {
        text-align: center;
    }
</style>
