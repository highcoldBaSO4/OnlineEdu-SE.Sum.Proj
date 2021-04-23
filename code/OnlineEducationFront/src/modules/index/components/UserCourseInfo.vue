<template>
    <div class="content-body-layout">
        <el-tabs type="border-card">
            <el-tab-pane>
                <span slot="label">课程公告</span>
                <CourseAnnouncement :announcements="courseInfo.notices"></CourseAnnouncement>
            </el-tab-pane>
            <el-tab-pane>
                <span slot="label">基本信息</span>
                <CourseInfo :course-info="courseInfo" class="info-layout"></CourseInfo>
            </el-tab-pane>
        </el-tabs>
    </div>
</template>

<script>
    import CourseAnnouncement from "./CourseAnnouncement";
    import CourseInfo from "./CourseInfo";
    export default {
        name: "UserCourseInfo",
        components: {CourseInfo, CourseAnnouncement},
        data() {
            return {
                announcements: [],
            }
        },
        computed: {
            courseInfo: function () {
                return this.$store.state.course.courseInfo;
            }
        }
    }
</script>

<style scoped>
    .info-layout {
        width: 950px;
        margin-left: auto;
        margin-right: auto;
    }
</style>
