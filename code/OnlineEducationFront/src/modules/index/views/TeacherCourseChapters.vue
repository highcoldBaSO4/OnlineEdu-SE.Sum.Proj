<template>
    <div>
        <el-header>
            <h1 class="titlesytle">课程章节</h1>
        </el-header>
        <el-main>
            <CourseChapters>
            </CourseChapters>
        </el-main>
    </div>
</template>

<script>
    import CourseChapters from "../components/CourseChapters"
    export default {
        name: "TeacherCourseChapters",

        components:{
            CourseChapters,
        },

        data(){
            return{
                loading:true,
            }

    }
    }
</script>

<style scoped>
    .titlesytle {
        text-align: center;
        padding-top: 20px
    }
</style>
