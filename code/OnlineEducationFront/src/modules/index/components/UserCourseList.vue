<template>
    <div class="content-body-layout">
        <el-tabs type="border-card">
            <el-tab-pane v-if="isTeacher">
                <span slot="label">教授课程</span>
                <div
                        v-loading="teachCourseLoading"
                        class="course-div"
                        :element-loading-background="loadingBackground"
                >
                    <CourseCard
                            v-for="(course, index) in teachCourses"
                            :key="index"
                            :course-info="course"
                    ></CourseCard>
                </div>
            </el-tab-pane>
            <el-tab-pane v-if="isStudent">
                <span slot="label">学习课程</span>
                <div
                        v-loading="learnCourseLoading"
                        class="course-div"
                        :element-loading-background="loadingBackground"
                >
                    <CourseCard
                            v-for="(course, index) in learnCourses"
                            :key="index"
                            :course-info="course"
                    ></CourseCard>
                </div>
            </el-tab-pane>
            <el-tab-pane v-if="isStudent">
                <span slot="label">担任助教</span>
                <div
                        v-loading="assistCourseLoading"
                        class="course-div"
                        :element-loading-background="loadingBackground"
                >
                    <CourseCard
                            v-for="(course, index) in assistCourses"
                            :key="index"
                            :course-info="course"
                    ></CourseCard>
                </div>
            </el-tab-pane>
            <el-tab-pane v-if="isTeacher">
                <span slot="label">创建课程</span>
                <TeacherCreateCourse></TeacherCreateCourse>
            </el-tab-pane>
        </el-tabs>
    </div>
</template>

<script>
    import CourseCard from "./CourseCard";
    import { mapGetters } from "vuex"
    import TeacherCreateCourse from "./TeacherCreateCourse";

    export default {
        name: "UserCourseList",
        components: {TeacherCreateCourse, CourseCard},
        data() {
            return {
                courseList: [
                    {
                        courseName: "数据库没成绩导论",
                        courseTime1: "2019-07-02",
                        courseTime2: "2019-08-02",
                        courseTeacher: "JBoss"
                    }
                ],
                teachCourses: [],
                learnCourses: [],
                assistCourses: [],
                teachCourseLoading: false,
                learnCourseLoading: false,
                assistCourseLoading: false,
                loadingBackground: "rgba(0,0,0,0.2)"
            }
        },
        methods: {
            getTeachCourses: function () {
                this.teachCourseLoading = true;
                this.$http.request({
                    url: "/api/users/info/courses/teach",
                    method: "get",
                    headers: this.$store.getters.authRequestHead
                }).then((response) => {
                    this.teachCourses = response.data;
                    this.teachCourseLoading = false;
                }).catch((error) => {
                    alert(error);
                    console.log(error.response);
                    this.teachCourseLoading = false;
                })
            },
            getAssistCourses: function () {
                this.assistCourseLoading = true;
                this.$http.request({
                    url: "/api/users/info/courses/assist",
                    method: "get",
                    headers: this.$store.getters.authRequestHead
                }).then((response) => {
                    this.assistCourses = response.data;
                    this.assistCourseLoading = false;
                }).catch((error) => {
                    alert(error);
                    console.log(error.response);
                    this.assistCourseLoading = false;
                })
            },
            getLearnCourses: function () {
                this.learnCourseLoading = true;
                this.$http.request({
                    url: "/api/users/info/courses/learn",
                    method: "get",
                    headers: this.$store.getters.authRequestHead
                }).then((response) => {
                    this.learnCourses = response.data;
                    this.learnCourseLoading = false;
                }).catch((error) => {
                    alert(error);
                    console.log(error.response);
                    this.learnCourseLoading = false;
                })
            }
        },
        computed: {
            ...mapGetters([
                'userRole'
                // 'isTeacher',
                // 'isStudent'
            ]),
            isTeacher: function() {
                if (this.userRole.indexOf("ROLE_TEACHING_ADMIN") !== -1) return true;
                else return false;
            },
            isStudent: function() {
                if (this.userRole.indexOf("ROLE_USER") !== -1) return true;
                else return false;
            }
        },
        mounted() {
            if (this.isTeacher) {
                this.getTeachCourses();
            }
            if (this.isStudent) {
                this.getLearnCourses();
                this.getAssistCourses();
            }
        }
    }
</script>

<style scoped>
    .course-div {
        min-height: 150px;
    }
</style>
