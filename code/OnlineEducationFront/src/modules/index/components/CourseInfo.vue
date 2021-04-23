<template>
    <div>
        <el-row class="row-layout">
            <el-col :span="24">
                <h2>{{ courseInfo.courseTitle }}</h2>
            </el-col>
        </el-row>
        <el-row class="row-layout">
            <el-col :span="12">
                <div class="float-left">教师：</div>
                <UserUnit :user="courseInfo.teacher" class="float-left"></UserUnit>
            </el-col>
            <el-col :span="12">
                <div class="float-left">助教：</div>
                <div v-for="(assistant, index) in courseInfo.courseAssistance"
                     :key="index"
                     class="float-left assi-unit"
                >
                    <UserUnit :user="assistant"></UserUnit>
                </div>
            </el-col>
        </el-row>
        <el-row class="row-layout">
            <el-col :span="12">
                时间：<DateRangeFormat :start="courseInfo.startDate" :end="courseInfo.endDate"></DateRangeFormat>
            </el-col>
            <el-col :span="12">
                地点：{{ courseInfo.location }}
            </el-col>
        </el-row>
        <el-row class="row-layout">
            <el-col :span="3">
                上课时间：
            </el-col>
            <el-col :span="21">
                <!--<p v-for="timeunit in courseInfo.timeSlots" :key="timeunit.id">-->
                    <!--{{ this.parseDay(timeunit.day) + " " + timeunit.start + " ~ " + timeunit.end }}-->
                <!--</p>-->
                <p v-for="timeunit in courseInfo.timeSlots" :key="timeunit.id">
                    {{ timeunit.day + " " + timeunit.start + " ~ " + timeunit.end }}
                </p>
            </el-col>
        </el-row>
        <el-row>
            <el-col :span="24">
                <h4>课程介绍</h4>
                <pre>{{ courseInfo.coursePrototype.description }}</pre>
            </el-col>
        </el-row>
    </div>
</template>

<script>
    import UserUnit from "./UserUnit";
    import DateRangeFormat from "./DateRangeFormat";

    export default {
        name: "CourseInfo",
        components: {DateRangeFormat, UserUnit},
        props: {
            courseInfo: {
                id: String,
                courseTitle: String,
                teacher: String,
                courseAssistance: Array,
                startDate: String,
                endDate: String,
                courseWeek: Array,
                location: String,
                courseDes: String,
            }
        },
        // methods: {
        //     parseDay: function (day) {
        //         switch (day) {
        //             case "MONDAY": return "星期一";
        //             case "TUESDAY": return "星期二";
        //             case "WEDNESDAY": return "星期三";
        //             case "THURSDAY": return "星期四";
        //             case "FRIDAY": return "星期五";
        //             case "SATURDAY": return "星期六";
        //             case "SUNDAY": return "星期日";
        //             default: return "emm"
        //         }
        //     }
        // }
    }
</script>

<style scoped>
    .assi-unit {
        margin-right: 20px;
    }

    .row-layout {
        margin-bottom: 20px;
    }
</style>
