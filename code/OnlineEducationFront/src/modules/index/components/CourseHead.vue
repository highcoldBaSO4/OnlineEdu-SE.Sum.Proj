<template>
    <div>
        <div class="image-div">
            <img :src="imgUrl" class="image-div">
        </div>
        <div class="title-div">
            <h2>{{ courseTitle }}</h2>
        </div>
    </div>
</template>

<script>
    export default {
        name: "CourseHead",
        computed: {
            courseTitle: function () {
                return this.$store.state.course.courseInfo.courseTitle;
            },
            imgUrl: function () {
                return this.$store.getters.getCourseImg;
            }
        }
    }
</script>

<style scoped>
    .image-div {
        width: 300px;
        height: 185px;
        background-color: white;
    }

    .title-div {
        width: 300px;
        text-align: center;
    }
</style>
