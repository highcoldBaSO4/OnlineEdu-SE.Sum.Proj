<template>
    <div>
        <el-header>
            <h1 class="titlesytle">学生信息查看</h1>
        </el-header>
        <el-main>
            <h3>助教信息</h3>
<!--            显示助教表格-->
            <el-table :data="assistantData"
                      class="usertable"
                      v-loading="loading"
                      stripe>
                <el-table-column >
                    <el-table-column type="index">
                    </el-table-column>
                    <el-table-column
                            prop="sno"
                            label="学号"
                            min-width="35%"
                            sortable="true">
                    </el-table-column>
                    <el-table-column
                            prop="username"
                            label="助教名"
                            min-width="35%"
                            sortable="true">
                    </el-table-column>
                    <el-table-column
                            prop="university"
                            label="学院"
                            min-width="25%"
                            sortable="true">
                    </el-table-column>
                    <el-table-column
                            prop="email"
                            label="邮箱"
                            min-width="50%"
                    ></el-table-column>
                </el-table-column>
            </el-table>
            <h3 style="float: left">学生信息</h3>
            <div style="float: right">
                <el-input class="padding"
                          v-model="search"
                          placeholder="请输入用户名"
                          prefix-icon="el-icon-search"/>
            </div>
<!--            学生信息显示table-->
            <el-table :data="StudentData.filter(data=>!search || data.username.includes(search))"
                      class="usertable"
                      v-loading="loading"
                      stripe>
                <el-table-column >
                    <el-table-column type="index">
                    </el-table-column>
                    <el-table-column
                            prop="sno"
                            label="学号"
                            min-width="35%"
                            sortable="true">
                    </el-table-column>
                    <el-table-column
                            prop="username"
                            label="学生名"
                            min-width="35%"
                            sortable="true">
                    </el-table-column>
                    <el-table-column
                            prop="university"
                            label="学院"
                            min-width="25%"
                            sortable="true">
                    </el-table-column>
                    <el-table-column
                            prop="email"
                            label="邮箱"
                            min-width="50%"
                    ></el-table-column>
                    <el-table-column
                            prop="sno"
                            label="操作"
                            min-width="40%">
                        <template slot-scope="scope">
                            <el-button type="button" icon="el-icon-star-off" @click="chooseAssistant(scope.$index, scope.row)">
                                任命助教
                            </el-button>
                        </template>
                    </el-table-column>
                </el-table-column>
            </el-table>
        </el-main>
    </div>
</template>

<script>
    export default {
        name: "TeacherStudentManage",

        data(){
            return{
                loading:true,

                search: '',

                StudentData: [],

                assistantData:this.$store.getters.getCourseInfo.teacherAssistants,
            }
        },

        methods:{
            showAllStudents(){
                // 该课程所有学生
                var that=this;
                this.$http.request({
                    url: '/api/courses/'+this.$store.getters.getCourseId+'/students',
                    method: "get",
                    headers: this.$store.getters.authRequestHead,
                })
                    .then(function (response) {
                        console.log(response.data);
                        // alert("请求成功");
                        that.StudentData = response.data;
                        that.loading=false;
                    })
                    .catch(function (error) {
                        console.log(error.response);
                        // alert("获取学生失败");
                    });
            },

            // 任命助教
            chooseAssistant(index, row){
                this.$confirm('此操作将任命该学生为助教, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    var that=this;
                    this.$http.request({
                        url: '/api/courses/'+this.$store.getters.getCourseId+'/teacherAssistant',
                        method: "post",
                        headers: this.$store.getters.authRequestHead,
                        params:{
                            teacherAssistantId:row.id,
                        }
                    })
                        .then(function (response) {
                            console.log(response.data);
                            that.showAllStudents();
                            that.$message.success("任命助教成功");
                        })
                        .catch(function (error) {
                            console.log(error.response);
                            that.$message.error("任命助教失败");
                        });
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消'
                    });
                });
            },

        },

        mounted() {
            this.showAllStudents();
        }
    }
</script>

<style scoped>
    .usertable {
        width: 100%;
        font-size: 15px;
        padding-bottom:20px;
    }

    .padding {
        padding: 8px;
    }

    .titlesytle {
        text-align: center;
        padding-top: 20px
    }
</style>
