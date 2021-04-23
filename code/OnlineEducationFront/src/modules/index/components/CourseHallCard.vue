<template>
    <div class="card-size">
        <el-card :body-style="bodyStyle">
            <div @click="enterCourse" class="card-body">
                <img :src="imgUrl" class="course-img">
                <div class="card-content">
                    <strong class="title-font">{{ courseInfo.courseTitle }}</strong>
                    <div class="line-layout">
                        <div class="float-left">
                            <UserUnit :user-id="courseInfo.teacher.id"></UserUnit>
                        </div>
                        <div class="float-right">
                            <!--<i class="el-icon-user"></i>
                            {{ courseInfo.courseStudent }}人-->
                        </div>
                        <div class="float-clear"></div>
                    </div>
                    <div class="line-layout">
                    <span>
                        <i class="el-icon-date"></i>
                        {{ dateFilter(courseInfo.startDate) }} ~ {{ dateFilter(courseInfo.endDate) }}
                    </span>
                    </div>
                </div>
            </div>
        </el-card>
    </div>
</template>

<script>
    import UserUnit from "./UserUnit";
    export default {
        name: "CourseHallCard",
        components: {UserUnit},
        props: {
            courseInfo: {
                imgUrl: String,
                id: Number,
                courseTitle: String,
                teacher: String,
                startDate: String,
                endDate: String,
                state: String
            }
        },
        data() {
            return {
                bodyStyle: {
                    padding: '0px',
                }
            }
        },
        methods: {
            enterCourse: function () {
                this.$store.commit("setCourseId", this.courseInfo.id);
                this.$router.push("/course")
            },
            dateFilter: function (date) {
                return date.substr(0,10);
            },
        },
        computed: {
            imgUrl: function () {
                return "http://202.120.40.8:30382/online-edu/static/" + this.courseInfo.avatarUrl
            }
        }
    }
</script>

<style scoped>
    .course-img {
        width: 296px;
        height: 186px;
    }

    .card-content {
        padding: 16px;
    }

    .card-size {
        width: 300px;
    }

    .card-body {
        border-color: transparent;
        border-style: solid;
        border-width: 1px;
    }

    .card-body:hover {
        cursor: pointer;
        border-color: black;
        border-style: solid;
        border-width: 1px;
    }

    .line-layout {
        margin-top: 15px;
    }
</style>
