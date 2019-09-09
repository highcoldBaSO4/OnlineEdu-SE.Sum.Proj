<template>
<!--    判断题组件-->
    <div id="AssignmentJudge">
        <h4>
            {{judge.content}}
        </h4>
        <div>
            <img v-for="(img, index) in judge.images" :key="index" :src="imgUrl(img)">
        </div>
        <div>
            <el-radio-group style="width: 80%" v-model="judge.myAnswer">
                <el-radio label="T">正确</el-radio>
                <el-radio label="F">错误</el-radio>
            </el-radio-group>
        </div>
        <p style="color:red" v-if="showAnswer">
            正确答案为：( {{judge.correctAnswer}} )
        </p>
    </div>
</template>

<script>
    export default {
        name: "AssignmentJudge",

        props: {
            judge: Object,
            showAnswer: false,
        },

        methods: {
            imgUrl(url) {
                return "http://202.120.40.8:30382/online-edu/static/" + url;
            }
        }
    }
</script>

<style scoped>

</style>
