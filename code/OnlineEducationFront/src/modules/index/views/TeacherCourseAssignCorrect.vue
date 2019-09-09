<template>
    <div>
        <el-header>
            <h1 class="titlesytle">作业{{this.$store.state.course.paperTitle}}批改</h1>
        </el-header>
        <el-main>
<!--            展示所有学生的答题情况-->
            <el-table :data="UserData.filter(data=>!search || data.username.includes(search))"
                      class="usertable"
                      v-loading="loading"
                      stripe>
                <el-table-column >
                    <el-table-column type="index">
                    </el-table-column>
                    <el-table-column
                            prop="student.sno"
                            label="学号"
                            min-width="35%"
                            sortable="true">
                    </el-table-column>
                    <el-table-column
                            prop="student.username"
                            label="学生名"
                            min-width="35%"
                            sortable="true">
                    </el-table-column>
                    <el-table-column
                            prop="state"
                            label="作业情况"
                            min-width="25%"
                    ></el-table-column>
                    <el-table-column
                            prop="student.sno"
                            label="操作"
                            min-width="40%">
                        <template slot-scope="scope">
                            <el-button type="button" icon="el-icon-edit" @click="handleEdit(scope.$index, scope.row)">
                                批改
                            </el-button>
                        </template>
                    </el-table-column>
                </el-table-column>
            </el-table>
        </el-main>
<!--        选择批改作业的次数-->
        <el-dialog :title="'选择作业'"
                   :visible.sync="dialogFormVisible"
                   :lock-scroll="false"
                   top="5%">
            <el-form :model="editForm" label-width="80px" ref="editForm">
                <el-form-item label="选择次数">
                    <el-radio-group v-model="timeChoose">
                        <el-radio :label="1" v-if="editForm.times>=1">第一次</el-radio>
                        <el-radio :label="2" v-if="editForm.times>=2">第二次</el-radio>
                        <el-radio :label="3" v-if="editForm.times>=3">第三次</el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-form>
            <span slot="footer" class="el-dialog__footer">
                <el-button @click.native="dialogFormVisible=false">取消</el-button>
                <el-button type="primary" @click="chooseData">选择</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "TeacherAssignCorrect",

        data(){
            return{
                assignId:0,

                loading:true,

                search: '',

                UserData: [],

                oneStuAnswer:[],

                dialogFormVisible:false,

                //编辑界面数据
                editForm: {
                    username:"",
                    score:"",
                },

                timeChoose:0,
            }
        },

        methods:{
            // 展示所有学生的答题情况
            showStuAssigns(){
                var that=this;
                this.$http.request({
                    url: '/api/courses/'+this.$store.getters.getCourseId+'/papers/'+this.$store.getters.getPaperId+'/state/all',
                    method: "get",
                    headers:this.$store.getters.authRequestHead,
                })
                    .then(function (response) {
                        console.log(response.data);
                        that.UserData=response.data;
                        that.loading=false;
                        // console.log("UserData"+that.UserData);
                        // alert("请求成功");
                    })
                    .catch(function (error) {
                        console.log(error.response);
                        // alert("学生答题情况请求失败");
                    });
            },

            //显示编辑界面
            handleEdit: function(index, row) {
                this.timeChoose=0;
                this.dialogFormVisible = true;
                this.editForm = Object.assign({}, row);
                // console.log(row.student.id);
                // 存储选中学生id
                this.$store.commit("setStudentSelectId", row.student.id);

                // 获取该学生答题数据
                var that=this;
                this.$http.request({
                    url: '/api/courses/'+this.$store.getters.getCourseId+'/papers/'+this.$store.getters.getPaperId+'/answer/'+row.student.id+'/all',
                    method: "get",
                    headers:this.$store.getters.authRequestHead,
                })
                    .then(function (response) {
                        // console.log("获取学生答题数据");
                        console.log(response.data);
                        that.oneStuAnswer=response.data;
                        // alert("请求成功");
                    })
                    .catch(function (error) {
                        console.log(error.response);
                        // alert("获取学生答题数据请求失败");
                    });
            },

            // 选择批改次数
            chooseData(){
                if (this.timeChoose===0) {
                    this.$message.warning("该学生尚未答题");
                    this.dialogFormVisible=false;
                }
                else{
                    this.dialogFormVisible=false;
                    // 把选择的答题的答案存起来
                    this.$store.commit("setPaperAnswers", this.oneStuAnswer[this.timeChoose-1]);
                    // console.log(this.$store.getters.getPaperAnswers);
                    this.$router.push("/course/manager/correctionSub");
                }
            },
        },

        mounted() {
            this.showStuAssigns();
        }
    }
</script>

<style scoped>
    .titlesytle {
        text-align: center;
        padding-top: 20px
    }

    .usertable {
        width: 100%;
        font-size: 15px;
        padding-bottom:20px;
    }
</style>
