<template>
    <div>
        <el-header>
            <h1 class="titlesytle">课程作业</h1>
        </el-header>
        <el-main>
            <el-table
                    :data="AssignData.filter(data => !search || data.name.toLowerCase().includes(search.toLowerCase()))"
                    style="width: 100%"
                    v-loading="loading">
                <el-table-column
                        prop="start"
                        label="开始时间"
                        sortable="true"
                        min-width="25%">
                </el-table-column>
                <el-table-column
                        prop="end"
                        label="结束时间"
                        sortable="true"
                        min-width="25%">
                </el-table-column>
                <el-table-column
                        prop="title"
                        label="作业名"
                        sortable="true"
                        min-width="40%">
                    <template scope="scope">
                        <el-link @click="loadPaperPage(scope.$index, scope.row)">{{scope.row.title}}</el-link>
                    </template>
                </el-table-column>
                <el-table-column
                        min-width="40%">
                    <template slot="header">
                        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">添加作业</el-button>
                    </template>
                    <template slot-scope="scope">
                        <el-button size="small"
                                   type="success"
                                   icon="el-icon-document-checked"
                                   @click="handleCorrection(scope.$index, scope.row)">
                            批改
                        </el-button>
                        <el-button size="small"
                                   icon="el-icon-edit"
                                   @click="handleEdit(scope.$index, scope.row)">
                            编辑
                        </el-button>
                        <el-button size="small"
                                   type="danger"
                                   icon="el-icon-delete"
                                   @click="handleDelete(scope.$index, scope.row)">
                            删除
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
        </el-main>
<!--        添加作业-->
        <el-dialog :title="'作业'"
                   :visible.sync="AssignVisible"
                   :lock-scroll="false"
                   top="5%"
                   width="80%">
            <el-form :model="AssignEditForm" label-width="80px" ref="AssignEditForm">
                <el-form-item label="作业名">
                    <el-input type="text" v-model="AssignEditForm.title"></el-input>
                </el-form-item>
                <el-form-item label="作业时间">
                    <el-col :span="11">
                        <el-date-picker placeholder="选择开始时间"
                                        type="datetime"
                                        v-model="AssignEditForm.start"
                                        value-format="yyyy-MM-dd HH:mm:ss"
                                        style="width: 100%;">
                        </el-date-picker>
                    </el-col>
                    <el-col class="line" :span="2">-</el-col>
                    <el-col :span="11">
                        <el-date-picker placeholder="选择结束时间"
                                        type="datetime"
                                        v-model="AssignEditForm.end"
                                        value-format="yyyy-MM-dd HH:mm:ss"
                                        style="width: 100%;">
                        </el-date-picker>
                    </el-col>
                </el-form-item>
                <el-form-item label="描述">
                    <el-input type="textarea" v-model="AssignEditForm.description"></el-input>
                </el-form-item>
            </el-form>
<!--            问题table-->
            <el-table :data="questions" height="300px" v-model="AssignEditForm.questionFormList" @selection-change="handleSelectionChange">
                <el-table-column type="selection" min-width="10%"></el-table-column>
                <el-table-column property="questionType" label="题型" sortable="true" min-width="20%"></el-table-column>
                <el-table-column property="content" label="题目" min-width="40%" show-overflow-tooltip="true"></el-table-column>
                <el-table-column property="score" label="分值" min-width="15%">
                    <template scope="scope">
                        <el-input placeholder="请输入分值" v-model="scope.row.score" type="number"></el-input>
                    </template>
                </el-table-column>
            </el-table>
            <span slot="footer" class="el-dialog__footer">
                <el-button @click.native="AssignVisible=false">取消</el-button>
                <el-button v-if="AssignStatus==='create'" type="primary" @click="createAssign">添加</el-button>
                <el-button v-else type="primary" @click="updateAssign">修改</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "TeacherCourseAssign",

        data(){
            return{
                loading: true,

                search:"",

                AssignData:[],

                AssignVisible:false,

                AssignEditForm: {
                    description:"",
                    title:"",
                    start:"",
                    end:"",
                    questionFormList:[
                        {
                            questionId:"",
                            questionNumber:1,
                            score:0,
                        }
                    ],
                },

                textMap: {
                    update: "Edit",
                    create: "Create"
                },

                AssignStatus:"",

                questions:[],

                multipleSelection:[],
            }
        },

        methods:{
            getThisCourseInfo(){
                this.AssignData=this.$store.getters.getCourseInfo.papers;
                this.loading=false;
            },

            // 显示增加作业弹窗
            handleAdd(){
                this.AssignEditForm=[];
                this.AssignStatus="create";
                this.AssignVisible=true;
            },

            // 批改作业
            handleCorrection:function(index, row){
                var typeCount=0;
                for (var x=0; x<row.questions.length; x++){
                    if (row.questions[x].paperWithQuestionsPrimaryKey.question.questionType==='SUBJECTIVE'){
                        typeCount++;
                    }
                }

                if (typeCount>0){
                    this.$store.commit("setPaperId", row.id);
                    this.$store.commit("setPaperTitle", row.title);
                    this.$router.push("/course/manager/correction");
                }
                else {
                    this.$message.warning("这份作业没有主观题");
                }
            },

            // 显示编辑作业弹窗
            handleEdit:function(index, row){
                this.AssignEditForm = Object.assign({}, row);
                this.AssignStatus="update";
                this.AssignVisible=true;
            },

            // 删除作业
            handleDelete:function (index, row) {
                this.$message.info(row.title+"已删除");
            },

            // 新增一份作业
            createAssign(){
                // 修改问题格式
                var finalQuestion=[];
                for (let x=0; x<this.multipleSelection.length; x++){
                    var tempquestion={
                        questionId:"",
                        questionNumber:1,
                        score:0,
                    };
                    tempquestion.questionId=this.multipleSelection[x].id;
                    tempquestion.score=this.multipleSelection[x].score;
                    tempquestion.questionNumber=x+1;
                    finalQuestion.push(tempquestion);
                }
                this.AssignEditForm.questionFormList=finalQuestion;
                // console.log(this.AssignEditForm);
                // console.log(this.AssignEditForm.start);
                var that=this;
                this.$http.request({
                    url: '/api/courses/'+this.$store.getters.getCourseId+'/papers',
                    method: "post",
                    headers: this.$store.getters.authRequestHead,
                    data:
                        {
                            title:this.AssignEditForm.title,
                            description:this.AssignEditForm.description,
                            start:this.AssignEditForm.start,
                            end:this.AssignEditForm.end,
                            questionFormList:finalQuestion,
                        }
                })
                    .then(function (response) {
                        console.log(response.data);
                        // that.getThisCourseInfo();
                        that.getCourse();
                        that.$message.success("添加作业成功");
                    })
                    .catch(function (error) {
                        console.log(error);
                        that.$message.error("添加作业失败："+error.response.data);
                    });

                this.AssignVisible=false;
            },

            // 编辑作业
            updateAssign(){

            },

            handleSelectionChange(val) {
                this.multipleSelection = val;
            },

            // 加载作业页面
            loadPaperPage: function (index, row) {
                this.$router.push({
                    name: "courseStudentPaper",
                    params: {
                        paperId: row.id
                    }
                });
            },

            getCourse(){
                var that=this;
                this.$http.request({
                    url: '/api/courses/'+this.$store.getters.getCourseId+'/info',
                    method: "get",
                    headers: this.$store.getters.authRequestHead,
                })
                    .then(function (response) {
                        console.log(response.data);
                        that.AssignData=response.data.course.papers;
                        that.loading=false;
                        // that.$store.commit("setCourseInfo",response.data);
                        // alert("请求成功");
                    })
                    .catch(function (error) {
                        console.log(error);
                        // alert("请求失败");
                    });
            }
        },

        mounted() {
            // 初始化questions
            var questionAll=this.$store.getters.getCourseInfo.coursePrototype.questions;
            for (let x=0; x<questionAll.length; x++){
                var tempquestion={
                    id:"",
                    question:"",
                    questionType:"",
                    score:"",
                };
                tempquestion.id=questionAll[x].id;
                tempquestion.questionType=questionAll[x].questionType;
                tempquestion.content=questionAll[x].content;
                tempquestion.score=0;
                this.questions.push(tempquestion);
            }
            console.log(this.questions);

            this.getThisCourseInfo();
        }
    }
</script>

<style scoped>
    .titlesytle {
        text-align: center;
        padding-top: 20px
    }
</style>
