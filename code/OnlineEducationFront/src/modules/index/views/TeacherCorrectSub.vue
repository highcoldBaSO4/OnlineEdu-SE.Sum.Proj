<template>
    <div>
        <el-header>
            <h1 class="titlesytle">主观题批改</h1>
        </el-header>
        <el-main>
<!--            主观题及答案展示-->
            <div v-for="question in allAnswers" :key="question.answerPrimaryKey.question.id">
                <div>
                    <h4>
                        问题: {{question.answerPrimaryKey.question.question}}
                    </h4>
                    <h4>
                        分值: {{question.score}}
                    </h4>
                    <p>
                        回答: {{question.answer}}
                    </p>
                </div>
<!--                展示图片-->
                <div v-for="image in question.imageUrl" :key="image">
                    <img v-if="image"
                         :src="'http://202.120.40.8:30382/online-edu/static/' + image + '?a=' + Math.random()"
                         class="avatar">
                </div>
<!--                打分和评语框-->
                <div style="width: 60%">
                    <el-input type="number" placeholder="请输入分数" v-model="question.grade"></el-input>
                    <el-input type="textarea" placeholder="请输入评语" v-model="question.comment"></el-input>
                </div>
            </div>
<!--            提交打分-->
            <el-button style="margin-top: 20px" @click="submitComments">提交</el-button>
        </el-main>
    </div>
</template>

<script>
    export default {
        name: "TeacherCorrectSub",

        data(){
            return{
                // questions:[],

                // 所有主观题
                allAnswers:[],

                // 获取题号的数组
                getQuestionNum:[],
            }
        },

        methods:{
            // 获取所有该学生的答案，把题号和分值存入
            showAllAnswers(){
                var questions=[];
                questions=this.$store.getters.getPaperAnswers.answers;

                var questionNums=[];
                questionNums=this.$store.getters.getPaperAnswers.paperAnswerPrimaryKey.paper.questions;

                // 获取答题答案
                for (var x=0; x<questions.length; x++) {
                    if (questions[x].answerPrimaryKey.question.questionType==='SUBJECTIVE'){
                        this.allAnswers.push(questions[x]);
                    }

                }
                // 获取题目信息
                for (var t=0; t<questionNums.length; t++){
                    if(questionNums[t].paperWithQuestionsPrimaryKey.question.questionType==='SUBJECTIVE'){
                        this.getQuestionNum.push(questionNums[t]);
                    }
                }
                // 把题号和分值存入
                var len1=this.allAnswers.length;
                var len2=this.getQuestionNum.length;
                for (var k=0; k<len1; k++){
                    for (var y=0; y<len2; y++){
                        if (this.allAnswers[k].answerPrimaryKey.question.id===this.getQuestionNum[y].paperWithQuestionsPrimaryKey.question.id) {
                            this.allAnswers[k].score=this.getQuestionNum[y].score;
                            // this.allAnswers[k].questionNumber=this.getQuestionNum[y].questionNumber;
                        }
                    }
                }
                console.log(this.allAnswers);
            },

            // 提交评分结果
            submitComments(){
                // 存储最终传输的数据
                var finalCorrect=[];
                for(var x=0; x<this.allAnswers.length; x++){
                    var temp={
                        comment: this.allAnswers[x].comment,
                        questionId:this.allAnswers[x].answerPrimaryKey.question.id,
                        score: this.allAnswers[x].grade,
                    };
                    finalCorrect.push(temp);
                }

                if (temp.score===""){
                    this.$message.warning("分数不能为空")
                }

                var that=this;
                this.$http.request({
                    url: '/api/courses/'+this.$store.getters.getCourseId+'/papers/'+this.$store.getters.getPaperId+'/answer/mark/'+this.$store.state.course.studentSelectId+"/"+this.$store.getters.getPaperAnswers.paperAnswerPrimaryKey.times,
                    method: "put",
                    headers:this.$store.getters.authRequestHead,
                    data:finalCorrect,
                })
                    .then(function (response) {
                        console.log("1");
                        console.log(response.data);
                        console.log("1");

                        that.$message.success("提交评分成功");
                        that.$router.push("/course/manager/correction");

                    })
                    .catch(function (error) {
                        console.log(error.response);
                        that.$message.error("提交评分失败："+error.response.data);

                    });
            }

        },

        mounted() {
            this.showAllAnswers();
        }
    }
</script>

<style scoped>
    .titlesytle {
        text-align: center;
        padding-top: 20px
    }
</style>
