<template>
    <el-card shadow="hover">
        <div class="card-content">
            <div class="course-img float-left">
                <img :src="imgUrl" class="course-img">
            </div>
            <div>
                <div class="float-left course-title">
                    <strong>{{ courseInfo.courseTitle }}</strong>
                    <p>
                        <UserUnit :user="courseInfo.teacher"></UserUnit>
                    </p>
                    <span class="location-font">地点：{{ courseInfo.location }}</span>
                </div>
                <div class="float-right">
                    <div class="float-clear course-time">
                        <span>
                            <i class="el-icon-date"></i>
                            <DateRangeFormat :start="courseInfo.startDate" :end="courseInfo.endDate"></DateRangeFormat>
                        </span>
                        <p>
                            <el-progress type="line" :percentage="datePercent"></el-progress>
                        </p>
                    </div>
                    <div class="enter-button">
                        <el-button @click="enterCourse">进入课程</el-button>
                    </div>
                </div>
            </div>
        </div>
    </el-card>
</template>

<script>
    import UserUnit from "./UserUnit";
    import DateRangeFormat from "./DateRangeFormat";
    export default {
        name: "CourseCard",
        components: {DateRangeFormat, UserUnit},
        props: {
            courseInfo: {
                id: Number,
                courseTitle: String,
                startDate: String,
                endDate: String,
                teacher: String,
            }
        },
        methods: {
            enterCourse: function () {
                this.$store.commit("setCourseId", this.courseInfo.id);

                // var that=this;
                // this.$http.request({
                //     url: '/api/courses/'+this.$store.getters.getCourseId+'/info',
                //     method: "get",
                //     headers: this.$store.getters.authRequestHead,
                // })
                //     .then(function (response) {
                //         console.log(response.data);
                //         that.$store.commit("setCourseInfo",response.data);
                //         // alert("请求成功");
                //     })
                //     .catch(function (error) {
                //         console.log(error);
                //         // alert("请求失败");
                //     });

                this.$router.push("/course");
            }
        },
        computed: {
            imgUrl: function () {
                return "http://202.120.40.8:30382/online-edu/static/" + this.courseInfo.avatarUrl
            },
            datePercent: function() {
                let startTime = new Date(this.courseInfo.startDate);
                let endTime = new Date(this.courseInfo.endDate);
                let nowTime = new Date();
                console.log(startTime);
                console.log(endTime);
                console.log(nowTime);
                if (nowTime >= endTime) return 100;
                else if (nowTime <= startTime) return 0;
                else {
                    let startSec = startTime.getTime();
                    return Math.trunc((nowTime.getTime() - startSec) / (endTime.getTime() - startSec) * 100);
                }
            }
        }
    }
</script>

<style scoped>

    .course-img {
        width: 250px;
        height: 155px;
        margin-bottom: 20px;
    }

    .course-title {
        padding-left: 40px;
        font-size: 23px;
    }

    .course-time {
        width: 200px;
        margin-top: 10px;
        font-size:14px;
    }

    .enter-button {
        margin-left: 50px;
        margin-top: 30px
    }

    .location-font {
        font-size: 15px;
    }
</style>
