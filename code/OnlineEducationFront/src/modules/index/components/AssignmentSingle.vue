<template>
<!--    单选题组件-->
    <div id="AssignmentSingle">
        <h4>
            {{single.content}}
        </h4>
        <div>
            <img v-for="(img, index) in single.images" :key="index" :src="imgUrl(img)">
        </div>
        <div>
            <el-radio-group style="width: 80%" v-model="single.myAnswer">
                <el-radio
                        v-for="(value, key) in single.options"
                        :key="key"
                        :label="key">
                    {{key + '. ' + value}}
                </el-radio>
            </el-radio-group>
        </div>
        <p style="color:red" v-if="showAnswer">
            正确答案为：( {{single.correctAnswer}} )
        </p>
    </div>
</template>

<script>
    export default {
        name: "AssignmentSingle",

        props: {
            single: Object,
            showAnswer: {
                default: false,
            },
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
