<template>
<!--    多选题组件-->
    <div id="AssignmentMulti">
        <h4>
            {{multi.content}}
        </h4>
        <div>
            <img v-for="(img, index) in multi.images" :key="index" :src="imgUrl(img)">
        </div>
        <div>
            <el-checkbox-group style="width: 80%"  v-model="answerList" @change="changeAnswer">
                <el-checkbox
                        v-for="(value, key) in multi.options"
                        :key="key"
                        :label="key">
                    {{key + '. ' + value}}
                </el-checkbox>
            </el-checkbox-group>
        </div>
        <p style="color:red" v-if="showAnswer">
            正确答案为：( {{multi.correctAnswer}} )
        </p>
    </div>
</template>

<script>
    export default {
        name: "AssignmentMulti",

        props: {
            multi: Object,
            showAnswer: false
        },
        data() {
            return {
                answerList: []
            }
        },
        methods: {
            changeAnswer: function () {
                let answerLine = "";
                this.answerList.sort();
                for (let i of this.answerList) {
                    answerLine += i;
                }
                this.multi.myAnswer = answerLine;
            },
            imgUrl(url) {
                return "http://202.120.40.8:30382/online-edu/static/" + url;
            }
        },
        mounted() {
            this.answerList = this.multi.myAnswer.split("");

        }
    }
</script>

<style scoped>

</style>
