<template>
    <el-card>
        <div slot="header">
            <div class="float-left">
                <strong class="title-font">{{ paperInfo.title }}</strong>
                <PaperTag :status="state" class="state-tag"></PaperTag>
                <span class="score-span" v-if="state === 'MARKED' && !isCourseTeacher">总分：{{ totalGrade }}</span>
            </div>
            <div class="float-right">
                <DateRangeFormat
                        class="float-right"
                        :start="paperInfo.start"
                        :end="paperInfo.end"
                        :show-minute="true"
                ></DateRangeFormat>
            </div>
            <div class="float-clear"></div>
            <div class="des-div" v-if="!isCourseTeacher">
                <pre>{{ paperInfo.description }}</pre>
                <p>剩余提交次数：{{ this.haveTime }}</p>
            </div>
        </div>
        <el-tabs>
            <el-tab-pane>
                <span slot="label">客观题</span>
                <AssignmentQuestion
                        v-for="question in objQuestions"
                        :key="question.id"
                        :question="question"
                        v-modal="answer"
                        :showgrade="showQuesScore"
                        :show-answer="showQuesAnswer"
                ></AssignmentQuestion>
            </el-tab-pane>
            <el-tab-pane>
                <span slot="label">主观题</span>
                <AssignmentSub
                        v-for="question in subjQuestions"
                        :key="question.id"
                        :sub="question"
                        :ref="question.id.toString()"
                ></AssignmentSub>
            </el-tab-pane>
            <div class="submit-div" v-if="!isCourseTeacher">
                <el-button
                        @click="submitAnswer('FINISHED')"
                        class="float-left"
                        :disabled="!allowSubmit"
                        :loading="submitLoading"
                >提交</el-button>
                <el-button
                        @click="submitAnswer('NOT_FINISH')"
                        class="float-right"
                        :disabled="!allowSubmit"
                        :loading="saveLoading"
                >暂存</el-button>
            </div>
        </el-tabs>
    </el-card>
</template>

<script>
    import AssignmentQuestion from "./AssignmentQuestion";
    import { mapGetters } from "vuex";
    import AssignmentSub from "./AssignmentSub";
    import DateRangeFormat from "./DateRangeFormat";
    import PaperTag from "./PaperTag";

    export default {
        name: "UserCoursePaper",
        components: {PaperTag, DateRangeFormat, AssignmentSub, AssignmentQuestion},
        props: {
            paperId: Number,
        },
        data() {
            return {
                paperInfo: {
                    questions: []
                },
                objQuestions: [],
                subjQuestions: [],
                state: "",
                totalGrade: 0,
                submitTime: 1,
                submitLoading: false,
                saveLoading: false
            }
        },
        methods: {
            submitAnswer: function (state) {
                if (state === "FINISHED") this.submitLoading = true;
                else this.saveLoading = true;
                let submitSubs = [];
                for (let sub of this.subjQuestions) {
                    console.log(sub.id.toString());
                    let subAnswer = this.$refs[sub.id.toString()][0].saveSub();
                    let submitFunc = () => {
                        return this.$http.request({
                            url: this.getPaperUrl + "/subjective",
                            method: "post",
                            data: subAnswer,
                            headers: {
                                ...this.$store.getters.authRequestHead,
                                'Content-Type': 'multipart/form-data'
                            }
                        });
                    };
                    submitSubs.push(submitFunc());
                    console.log(submitSubs);
                }
                this.$http.all(submitSubs).then(() => {
                    let answerList = [];
                    for (let question of this.objQuestions) {
                        let answerUnit = {
                            answer: question.myAnswer,
                            questionId: question.id
                        };
                        answerList.push(answerUnit);
                    }
                    console.log(answerList);
                    this.$http.request({
                        url: this.getPaperUrl,
                        method: "post",
                        headers: this.$store.getters.authRequestHead,
                        data: {
                            answerList,
                            state: "TEMP_SAVE"
                        },
                        withCredentials: true
                    }).then((response) => {
                        console.log(response.data);
                        this.$http.request({
                            url: this.getPaperUrl + "/state/change",
                            method: "put",
                            headers: this.$store.getters.authRequestHead,
                            params: {
                                state: state
                            }
                        }).then(() => {
                            if (state === "NOT_FINISH") {
                                this.$root.success("暂存成功！");
                                this.saveLoading = false;
                            }
                            else if (state === "FINISHED") {
                                this.$root.success("提交成功！");
                                this.submitLoading = false;
                            }
                            this.initialPaper();
                        }).catch((error) => {
                            this.$root.error(error);
                            console.log(error.response);
                            this.submitLoading = false;
                            this.saveLoading = false;
                        })
                    });
                }).catch((error) => {
                    alert(error);
                    console.log(error.response);
                    this.submitLoading = false;
                    this.saveLoading = false;
                })
            },
            initialPaper: function () {
                this.paperInfo = this.$store.getters.getPaperById(parseInt(this.paperId));
                this.objQuestions = [];
                this.subjQuestions = [];
                this.$http.request({
                    url: this.getPaperUrl,
                    method: "get",
                    headers: this.$store.getters.authRequestHead
                }).then((response) => {
                    let answerList = [];
                    response.data.sort((a,b) => {
                        if (a.paperAnswerPrimaryKey.times < b.paperAnswerPrimaryKey.times) return 1;
                        else return -1;
                    });
                    console.log(response.data);
                    if (response.data.length !== 0) {
                        answerList = response.data[0].answers;
                        this.state = response.data[0].state;
                        this.totalGrade = response.data[0].grade;
                        this.submitTime = response.data[0].paperAnswerPrimaryKey.times;
                    }
                    else {
                        this.state = "NOT_START";
                    }
                    this.paperInfo.questions.sort((a, b) => {
                        if (a.questionNumber <= b.questionNumber) return -1;
                        else return 1;
                    });
                    for (let scanQues of this.paperInfo.questions) {
                        let fetchQues = Object.assign({}, scanQues.paperWithQuestionsPrimaryKey.question, {
                            score: scanQues.score,
                            myScore: 0,
                            myAnswer: "",
                            myImg: []
                        });
                        for (let i in answerList) {
                            if (answerList[i].answerPrimaryKey.question.id === fetchQues.id) {
                                fetchQues.myScore = answerList[i].grade;
                                fetchQues.myAnswer = answerList[i].answer;
                                answerList.splice(i, 1);
                                i--;
                            }
                        }
                        if (fetchQues.questionType !== "SUBJECTIVE") {
                            this.objQuestions.push(fetchQues);
                        }
                        else {
                            this.subjQuestions.push(fetchQues);
                        }
                    }
                    console.log(this.paperInfo);
                }).catch((error) => {
                    alert(error);
                    console.log(error.response);
                })
            }
        },
        computed: {
            ...mapGetters([
                "getPaperById",
                'isCourseTeacher'
            ]),
            getPaperUrl: function () {
                return this.$store.getters.getCourseUrl + "papers/" + this.paperInfo.id + "/answer";
            },
            showQuesScore: function () {
                switch (this.state) {
                    case "NOT_MARK":
                    case "MARKED":
                        return true;
                    default:
                        return false;
                }
            },
            showQuesAnswer: function() {
                return (this.state === "MARKED")
            },
            haveTime: function () {
                let haveTimes = 3 - this.submitTime;
                if (this.state !== "FINISHED") {
                    haveTimes++;
                }
                return haveTimes;
            },
            allowSubmit: function () {
                let nowTime = this.$root.dateToString(new Date(), true);
                console.log(nowTime);
                return !(this.haveTime === 0 || this.state === "NOT_MARK" || this.state === "MARKED" || nowTime > this.paperInfo.end || nowTime < this.paperInfo.start);
            }
        },
        created() {
            this.initialPaper();
        }
    }
</script>

<style scoped>
    .title-font {
        font-size: 25px;
    }

    .des-div {
        margin-top: 20px;
    }

    .submit-div {
        width: 200px;
        margin-left: auto;
        margin-right: auto;
        margin-top: 20px;
    }

    .score-span {
        margin-left: 50px;
        color: darkred;
    }

    .state-tag {
        margin-left: 50px
    }
</style>
