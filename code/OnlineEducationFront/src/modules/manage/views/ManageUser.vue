<template>
    <div>
        <div>
            <h3>用户管理</h3>
            <div class="divleft">
                <el-input
                        class="padding"
                        v-model="search"
                        placeholder="请输入用户名"
                        prefix-icon="el-icon-search"/>
                <el-upload
                        class="upload-demo"
                        ref="upload"
                        action="/api/users/bulkImport"
                        :http-request="uploadExcel"
                        :on-preview="handlePreview"
                        :before-upload="beforeUpload"
                        :on-remove="handleRemove"
                        :limit="3"
                        :on-exceed="handleExceed"
                        :auto-upload="false"
                        :file-list="fileList">
                    <el-button slot="trigger" size="small" type="primary" style="margin-right: 10px">点击选择用户信息</el-button>
                    <el-button size="small" type="success" @click="submitUpload">点击上传</el-button>
                    <div slot="tip" class="el-upload__tip">只能上传.xls或.xlsx文件</div>
                </el-upload>
                <el-progress v-if="excelFlag===true" :percentage="excelUploadPercent"></el-progress>
            </div>
            <div class="divright">
<!--                <el-button @click="handleAdd">新增</el-button>-->
            </div>
            <el-table :data="UserData.filter(data=>!search || data.username.includes(search))"
                      class="usertable"
                      v-loading="loading"
                      highlight-current-row="true">
                <el-table-column >
                    <el-table-column type="index">
                    </el-table-column>
                    <el-table-column
                            prop="sno"
                            label="用户编号"
                            min-width="35%"
                            sortable>
                    </el-table-column>
                    <el-table-column
                            prop="username"
                            label="用户名"
                            min-width="35%"
                            sortable>
                    </el-table-column>
                    <el-table-column
                            prop="role"
                            label="用户身份"
                            min-width="25%"
                            sortable>
                    </el-table-column>
                    <el-table-column
                            prop="tel"
                            label="电话"
                            min-width="50%"
                    ></el-table-column>
                    <el-table-column
                            prop="email"
                            label="邮箱"
                            min-width="50%"
                    ></el-table-column>
                    <el-table-column
                            prop="userId"
                            label="操作"
                            fixed="right"
                            min-width="40%">
                        <template slot-scope="scope">
                            <span v-if="scope.row.role!=='管理员'">
                                <el-button type="primary" size="small" @click="handleEdit(scope.$index, scope.row)">
                                    修改
                                </el-button>
                                <el-button type="danger" size="small" @click="handleDel(scope.$index, scope.row)">
                                    删除
                                </el-button>
                            </span>
                            <span v-else>无法操作</span>
                        </template>
                    </el-table-column>
                </el-table-column>
            </el-table>
        </div>
        <!--编辑增加页面弹窗-->
        <el-dialog
                :title="textMap[dialogStatus]"
                :visible.sync="dialogFormVisible"
                :lock-scroll="false"
                top="5%">
            <el-form :model="editForm" :rules="formRule" label-width="80px" ref="editForm">
                <el-form-item label="用户编号">
                    <el-input type="text" v-model="editForm.sno"></el-input>
                </el-form-item>
                <el-form-item label="用户名">
                    <span>{{editForm.username}}</span>
                </el-form-item>
<!--                只有用户为学生是才显示是否授权为教师的选项-->
                <span v-if="editForm.boolrole===false">
                    <el-form-item label="用户身份">
                        <el-checkbox label="教师" v-model="rolecheck">授权为教师</el-checkbox>
                    </el-form-item>
                </span>
                <el-form-item label="电话" prop="tel">
                    <el-input type="text" v-model="editForm.tel"></el-input>
                </el-form-item>
                <el-form-item label="邮箱" prop="email">
                    <el-input type="text" v-model="editForm.email"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="el-dialog__footer">
                <el-button @click.native="dialogFormVisible=false">取消</el-button>
                <el-button v-if="dialogStatus==='create'" type="primary" @click="createData">添加</el-button>
                <el-button v-else type="primary" @click="updateData">修改</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    import getHeader from "../managerRequestHeader.js";
    import Rules from "../../index/rules.js"

    export default {
        name: "ManageUser",

        data(){
            return{
                search: '',

                UserData: [],

                loading:true,

                dialogFormVisible:false,

                dialogStatus: "",

                textMap: {
                    update: "Edit",
                    create: "Create"
                },

                //编辑界面数据
                editForm: {
                    id:"",
                    sno:"",
                    username: "",
                    email:"",
                    tel:"",
                    role:"学生",
                    boolrole:false,
                },

                rolecheck:"",

                // 校验规则
                formRule: {
                    tel: Rules.telRule,
                    email: Rules.emailRule,
                },

                fileList:[],

                excelFlag:false,

                excelUploadPercent:0,

            }
        },

        methods:{
            showAllUsers(){
                var that=this;
                this.$axios.request({
                    url: '/api/users/info/all',
                    method: "get",
                    headers: getHeader.requestHeader()
                })
                    .then(function (response) {
                        console.log(response.data);
                        // alert("请求成功");
                        that.UserData = response.data;

                        if (response.data==="Unauthorized")
                        {
                            that.$message.warning("您没有管理员权限！");
                        }

                        // 存储role
                        for (let index=0; index<that.UserData.length; index++)
                        {
                            var temprolearray=[];
                            temprolearray=response.data[index].roles;
                            // console.log(temprolearray[0].role);

                            for (let x=0; x<temprolearray.length; x++)
                            {
                                var temprole=temprolearray[x].role;
                                // console.log(temprolearray[x].role);

                                if (temprole==="ROLE_ADMIN"){
                                    that.UserData[index].role="管理员";
                                }
                                else if(temprole==="ROLE_TEACHING_ADMIN") {
                                    that.UserData[index].role="教师";
                                }
                                else{
                                    that.UserData[index].role="学生";
                                }
                            }
                        }

                        that.loading=false;

                    })
                    .catch(function (error) {
                        console.log(error.response);
                        // alert("请求失败");
                    })
            },

            submitUpload() {
                this.$refs.upload.submit();
            },

            handleRemove(file, fileList) {
                console.log(file, fileList);
            },

            handlePreview(file) {
                console.log(file);
            },

            handleExceed(files, fileList) {
                this.$message.warning(`当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
            },

            // 上传前校验格式
            beforeUpload(file) {
                let Xls = file.name.split('.');

                if (Xls[1] === 'xls' || Xls[1] === 'xlsx') {
                    return file
                } else {
                    this.$message.error('上传文件只能是 xls/xlsx 格式!');
                    return false;
                }
            },

            // 上传文件
            uploadExcel(file){
                console.log("正在上传文件");
                this.$message.info("正在上传文件");

                // 进度条
                // this.excelFlag = true;

                let param = new FormData();
                param.append('excel',file.file);

                var that=this;
                this.$axios.request(
                    {
                        url: '/api/users/bulkImport',
                        method: "post",
                        headers: {Authorization: "Bearer " + localStorage.getItem("managerToken") ,'Content-Type':'multipart/form-data'},
                        data:param,
                        onUploadProgress: (event) => {
                            // 监听上传进度
                            if (event.lengthComputable) {
                                let val = (event.loaded / event.total * 100).toFixed(0);
                                that.excelFlag = true;
                                that.excelUploadPercent = parseInt(val);
                                // console.log(val);
                            }
                        }
                    },
                )
                    .then(function (response) {
                        console.log(response.data);
                        that.$message.success('上传成功');
                        that.excelFlag=false;
                        that.showAllUsers();
                    })
                    .catch(function (error) {
                        console.log(error.response.data);
                        that.$message.error(error.response.data);
                        that.excelFlag=false;
                    });
            },

            // 进度条
            uploadProgress(event,file,fileList){
                this.excelFlag = true;
                // this.excelUploadPercent = file.percentage.toFixed(0);

                this.excelUploadPercent=Math.floor(event.percent);
                console.log(this.excelUploadPercent);

            },

            // 删除学生
            handleDel:function(index,row){
                this.$message.info(row.username+"已删除");
            },

            //显示编辑界面
            handleEdit: function(index, row) {
                this.dialogStatus = "update";
                this.dialogFormVisible = true;
                this.editForm = Object.assign({}, row);
                if(this.editForm.role==='学生'){
                    this.editForm.boolrole=false;
                }
                else {
                    this.editForm.boolrole=true;
                }
            },

            //显示新增界面
            handleAdd: function() {
                this.dialogStatus = "create";
                this.dialogFormVisible = true;
                this.editForm = {
                    sno:"",
                    username: "",
                    email:"",
                    role:'学生',
                    boolrole:false,
                }
            },

            createData(){
                this.$message.success("用户添加成功");
                this.dialogFormVisible=false;
            },

            // 编辑信息
            updateData(){
                var that=this;
                // 把学生修改为老师
                if (this.rolecheck===true){
                    this.$axios.request({
                        url: '/api/auth/'+this.editForm.id+'/teachingAdmin',
                        method: "post",
                        headers: getHeader.requestHeader()
                    })
                        .then(function (response) {
                            console.log(response.data);
                            // alert("请求成功");
                        })
                        .catch(function (error) {
                            console.log(error);
                            // alert("请求失败");
                            that.$message.error(error.response.data);
                        });
                }
                // 修改用户信息
                this.$axios.request({
                    url: '/api/users/'+this.editForm.id+'/info/modify',
                    method: "post",
                    headers: getHeader.requestHeader(),
                    data:{
                        email:this.editForm.email,
                        tel:this.editForm.tel,
                        sno:this.editForm.sno,
                    }
                })
                    .then(function (response) {
                        console.log(response.data);
                        if(response.data.id===that.editForm.id)
                        {
                            that.rolecheck="";
                            that.showAllUsers();
                            that.dialogFormVisible=false;
                            that.$message.success("修改成功");
                        }

                    })
                    .catch(function (error) {
                        console.log(error);
                        that.$message.error(error.response.data);
                    })
            }
        },

        mounted() {
            this.showAllUsers();
        }
    }
</script>

<style scoped>
    .divleft {
        float: left;
    }

    .divright {
        float: right;
    }

    .padding {
        padding: 8px;
    }

    .usertable {
        width: 100%;
        font-size: 15px;
        padding-bottom:20px;
    }

</style>
