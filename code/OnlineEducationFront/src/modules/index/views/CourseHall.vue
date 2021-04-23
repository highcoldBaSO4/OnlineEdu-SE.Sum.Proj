<template>
    <div ref="courseHall" class="course-hall">
        <CourseHallCard
                v-for="course in courseList"
                :key="course.courseId"
                :course-info="course"
                class="float-left course-layout"
        ></CourseHallCard>
    </div>
</template>

<script>
    import CourseHallCard from "../components/CourseHallCard";
    export default {
        name: "CourseHall",
        components: {CourseHallCard},
        data() {
            return {
                courseList: []
            }
        },
        mounted() {
            let loading = this.$loading({
                target: this.$refs["courseHall"],
                text: "加载课程列表",
                fullscreen: false
            });
            this.$http.request({
                url: "/api/courses/all/info",
                method: "get",
                headers: this.$store.getters.authRequestHead
            }).then((response) => {
                console.log(response.data);
                this.courseList = response.data;
                loading.close();
            }).catch((error) => {
                alert(error);
                console.log(error.response);
                loading.close();
            })
        }
    }
</script>

<style scoped>
    .course-layout {
        margin: 20px 15px;
    }

    .course-hall {
        min-height: 150px;
    }
</style>
