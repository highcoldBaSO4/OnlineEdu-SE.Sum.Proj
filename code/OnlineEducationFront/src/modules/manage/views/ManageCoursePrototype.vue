<template>
    <div>
        <div>
            <h3>课程原型管理</h3>
            <div class="divleft">
                <el-input
                        class="padding"
                        v-model="search"
                        placeholder="请输入课程名"
                        prefix-icon="el-icon-search"/>
            </div>
            <div class="divright">
                <el-button @click="handleAdd">新增</el-button>
            </div>
            <el-table :data="CourseData.filter(data=>!search || data.courseName.includes(search))"
                      class="coursetable"
                      v-loading="loading"
                      highlight-current-row="true">
                <el-table-column >
                    <el-table-column type="index">
                    </el-table-column>
                    <el-table-column
                            prop="title"
                            label="原型名称"
                            min-width="30%"
                            sortable
                    ></el-table-column>
                    <el-table-column
                            prop="description"
                            label="描述"
                            min-width="40%"
                            sortable
                    ></el-table-column>
<!--                    管理可使用该课程原型的用户-->
                    <el-table-column
                            prop="user"
                            label="审核用户"
                            min-width="20%"
                            sortable>
                        <template slot-scope="scope">
                            <el-button type="info"
                                       plain
                                       size="small"
                                       @click="showUsers(scope.$index, scope.row)">
                                用户详情
                            </el-button>
                        </template>
                    </el-table-column>
                    <!--                    显示课程状态的tab-->
                    <el-table-column
                            prop="courseState"
                            label="状态"
                            min-width="25%"
                            sortable>
                        <template slot-scope="scope">
                            <el-tag :type="scope.row.courseStateTemp">
                                {{scope.row.courseState}}
                            </el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column
                            prop="id"
                            label="操作"
                            min-width="40%">
                        <template slot-scope="scope">
                            <el-button type="primary" size="small" @click="handleEdit(scope.$index, scope.row)">
                                修改
                            </el-button>
                            <el-button type="danger" size="small" @click="handleDel(scope.$index, scope.row)">
                                删除
                            </el-button>
                        </template>
                    </el-table-column>
                </el-table-column>
            </el-table>
        </div>
        <!--课程原型编辑页面弹窗-->
        <el-dialog :title="textMap[dialogStatus]"
                   :visible.sync="dialogFormVisible"
                   :lock-scroll="false"
                   top="5%">
            <!--            课程原型的基本信息-->
            <el-form :model="editForm" label-width="80px" ref="editForm">
                <el-form-item label="原型名">
                    <el-input type="text" v-model="editForm.title"></el-input>
                </el-form-item>
                <el-form-item label="描述">
                    <el-input type="text" v-model="editForm.description"></el-input>
                </el-form-item>
                <el-form-item label="课程状态" v-if="dialogStatus==='update'">
                    <el-radio-group v-model="editForm.courseState">
<!--                        <el-radio label="待审核"></el-radio>-->
                        <el-radio label="可使用"></el-radio>
                        <el-radio label="未通过"></el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-form>
            <span slot="footer">
                <el-button @click.native="dialogFormVisible=false">取消</el-button>
                <el-button v-if="dialogStatus==='create'" type="primary" @click="createData">
                    添加
                </el-button>
                <el-button v-else type="primary" @click="updateData">
                    修改
                </el-button>
            </span>
        </el-dialog>
<!--        课程原型申请使用审核弹窗-->
        <el-dialog :title="'课程原型相关用户'"
                   :visible.sync="UserDialogVisible"
                   :lock-scroll="false"
                   top="5%">
<!--            显示所有通过审核的用户和需要审核的用户-->
            <el-table :data="UserForm" height="500px">
                <el-table-column type="index"></el-table-column>
                <el-table-column property="username"
                                 label="用户名"
                                 min-width="20%"
                                 sortable></el-table-column>
                <el-table-column property="state"
                                 label="状态"
                                 min-width="50%"
                                 sortable>
                    <template slot-scope="scope">
                        <el-radio-group v-model="scope.row.userState">
                            <el-radio label="未通过">未通过</el-radio>
                            <el-radio label="可使用">可使用</el-radio>
                            <el-radio label="待审核">待审核</el-radio>
                        </el-radio-group>
                    </template>
                </el-table-column>
            </el-table>
            <span slot="footer">
                <el-button @click.native="UserDialogVisible=false">取消</el-button>
                <el-button type="primary" @click="manageUser">修改</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    import getHeader from "../managerRequestHeader.js";

    export default {
        name: "ManageCoursePrototype",

        data(){
            return{
                search:"",

                CourseData: [],

                loading:true,

                dialogFormVisible:false,

                UserDialogVisible:false,

                dialogStatus: "",

                textMap: {
                    update: "编辑",
                    create: "新增"
                },

                //编辑界面数据
                editForm: {
                    id:"",
                    title:"",
                    description:"",
                    courseState:"",
                    state:"",
                },

                UserForm:[],

                UserEditForm:{
                    username:"",
                    state:"",
                },

                CourseIdtoUser:"",
            }
        },

        methods:{
            showAllCoursePrototypes(){
                var that=this;
                this.$axios.request({
                    url: '/api/coursePrototypes/info/all',
                    method: "get",
                    headers: getHeader.requestHeader()
                })
                    .then(function (response) {
                        console.log(response.data);
                        // alert("请求成功");
                        that.CourseData=response.data;

                        // 管理状态
                        for (let index=0; index<that.CourseData.length; index++)
                        {
                            var tempState;
                            tempState=response.data[index].state;

                            if (tempState==='NOT_DECIDE') {
                                that.CourseData[index].courseStateTemp="warning";
                                that.CourseData[index].courseState="待审核";
                            }
                            else if (tempState==='USING') {
                                that.CourseData[index].courseStateTemp = 'success';
                                that.CourseData[index].courseState='可使用';
                            }
                            else if (tempState==='DENIAL'){
                                that.CourseData[index].courseStateTemp = 'danger';
                                that.CourseData[index].courseState='未通过';
                            }
                        }

                        that.loading=false;
                    })
                    .catch(function (error) {
                        console.log(error.response);
                        that.$message.error(error.response.data);
                    })
            },

            handleDel:function(index,row){
                this.$message.info(row.courseName+"已删除");
            },

            //显示编辑界面
            handleEdit: function(index, row) {
                this.dialogStatus = "update";
                this.dialogFormVisible = true;
                this.editForm = Object.assign({}, row);
            },

            //显示新增界面
            handleAdd: function() {
                this.dialogStatus = "create";
                this.dialogFormVisible = true;
                this.editForm = {
                    id:"",
                    title:"",
                    description:"",
                    courseState:"",
                }
            },

            // 添加课程原型
            createData(){
                var that=this;

                this.$axios.request({
                    url: '/api/coursePrototypes/',
                    method: "post",
                    headers: getHeader.requestHeader(),
                    data:{
                        title:this.editForm.title,
                        description:this.editForm.description,
                    }
                })
                    .then(function (response) {
                        console.log(response.data);

                        that.$message.success("添加课程原型成功");

                        that.showAllCoursePrototypes();
                        that.dialogFormVisible=false;
                    })
                    .catch(function (error) {
                        console.log(error);
                        // alert("请求失败");
                        that.$message.error(error.response.data);
                    });


            },

            // 修改课程原型状态
            updateData(){
                var that=this;

                if (this.editForm.courseState==='未通过'){
                    this.editForm.state='disapproval';
                }
                else if(this.editForm.courseState==='可使用'){
                    this.editForm.state='approval';
                }

                this.$axios.request({
                    url: '/api/coursePrototypes/'+this.editForm.id+'/create',
                    method: "post",
                    headers: getHeader.requestHeader(),
                    params:{
                        decision:this.editForm.state,
                    }
                })
                    .then(function (response) {
                        console.log(response.data);

                        that.$message.success("修改成功");
                        that.showAllCoursePrototypes();
                        that.dialogFormVisible=false;
                        // alert("请求成功");
                    })
                    .catch(function (error) {
                        console.log(error);
                        // alert("请求失败");
                        that.$message.error(error.response.data);
                    });
            },

            // 展示所有与该课程原型相关的用户
            showUsers:function(index,row){
                var that=this;

                that.CourseIdtoUser=row.id;
                that.UserForm=[];

                this.$axios.request({
                    url: '/api/coursePrototypes/'+row.id+'/applications',
                    method: "get",
                    headers: getHeader.requestHeader(),
                })
                    .then(function (response) {
                        console.log(response.data);

                        for (let x=0; x<response.data.length; x++){
                            var res=response.data[x];
                            var userState;

                            if (res.state==='NOT_DECIDE'){
                                userState="待审核";
                            }
                            else if (res.state==='APPROVAL') {
                                userState='可使用';
                            }
                            else if (res.state==='DISAPPROVAL'){
                                userState='未通过';
                            }

                            var tempTeachingAdmin=res.applicationForCoursePK.teachingAdmin;

                            var tempUser={
                                id:tempTeachingAdmin.id,
                                username:tempTeachingAdmin.username,
                                userState:userState,
                                decision:"",
                            };

                            that.UserForm.push(tempUser);
                        }

                        console.log(that.UserForm);
                        // that.showAllCoursePrototypes();
                        // that.dialogFormVisible=false;
                        // alert("请求成功");
                    })
                    .catch(function (error) {
                        console.log(error);
                        // alert("请求失败");
                    });

                this.UserDialogVisible=true;
            },

            // 审核教师对课程原型使用的申请
            manageUser:function () {
                var that=this;

                console.log(that.UserForm);

                for (let x=0; x<that.UserForm.length; x++)
                {
                    if (that.UserForm[x].userState==='未通过'){
                        that.UserForm[x].decision='disapproval';
                    }
                    else if(that.UserForm[x].userState==='可使用'){
                        that.UserForm[x].decision='approval';
                    }

                    this.$axios.request({
                        url: '/api/coursePrototypes/apply',
                        method: "post",
                        headers: getHeader.requestHeader(),
                        params:{
                            applicant_id:this.UserForm[x].id,
                            coursePrototype_id:this.CourseIdtoUser,
                            decision:this.UserForm[x].decision,
                        }
                    })
                        .then(function (response) {
                            console.log(response.data);

                            that.$message.success("已审核");
                            that.showAllCoursePrototypes();
                            that.UserDialogVisible=false;
                            // alert("请求成功");
                        })
                        .catch(function (error) {
                            console.log(error);
                            // alert("请求失败");
                            that.$message.error(error.response.data);
                        });

                    // console.log(that.UserForm[x]);
                }
            },

        },

        mounted() {
            this.showAllCoursePrototypes();
        }
    }
</script>

<style scoped>
    .divleft {
        float: left;
    }

    .divleftmargin{
        float: left;
        margin-right: 20px;
    }

    .divright {
        float: right;
    }

    .padding {
        padding: 8px;
    }

    .coursetable {
        width: 100%;
        font-size: 15px;
        padding-bottom:20px;
    }
</style>
