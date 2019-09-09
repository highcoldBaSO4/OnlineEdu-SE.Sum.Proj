<template>
    <el-card :body-style="{ paddingTop: '1px' }">
        <div slot="header">
            <strong>{{ questionType }}</strong>
            <span v-if="showgrade" class="my-grade">得分：{{question.myScore}}</span>
            <div class="float-right">
                {{ question.score }}分
            </div>
        </div>
        <AssignmentJudge v-if="question.questionType === 'T_OR_F'" :judge="question" ></AssignmentJudge>
        <AssignmentSingle v-else-if="question.questionType === 'SINGLE_ANSWER'" :single="question"></AssignmentSingle>
        <AssignmentMulti v-else-if="question.questionType === 'MULTIPLE_ANSWER'" :multi="question"></AssignmentMulti>
        <AssignmentSub v-else-if="question.questionType === 'SUBJECTIVE'" :sub="question"></AssignmentSub>
        <p style="color:red" v-if="showAnswer">
            正确答案为：( {{question.answer}} )
        </p>
    </el-card>
</template>

<script>
    import AssignmentJudge from "./AssignmentJudge";
    import AssignmentSingle from "./AssignmentSingle";
    import AssignmentMulti from "./AssignmentMulti";
    import AssignmentSub from "./AssignmentSub";
    export default {
        name: "AssignmentQuestion",
        components: {AssignmentSub, AssignmentMulti, AssignmentSingle, AssignmentJudge},
        props: {
            question: Object,
            showgrade: {
                default: false
            },
            showAnswer: {
                default: false
            }
        },
        methods: {

        },
        computed: {
            questionType: function () {
                switch (this.question.questionType) {
                    case "T_OR_F": return "判断题";
                    case "SINGLE_ANSWER": return "单选题";
                    case "MULTIPLE_ANSWER": return "多选题";
                    default: return "什么鬼"
                }
            }
        }
    }
</script>

<style scoped>
    .my-grade {
        color: red;
        margin-left: 60px
    }
</style>
