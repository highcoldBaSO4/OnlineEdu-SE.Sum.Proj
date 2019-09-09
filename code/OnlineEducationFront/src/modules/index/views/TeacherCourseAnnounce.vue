<template>
    <div>
        <el-header>
            <h1 class="titlesytle">课程公告</h1>
        </el-header>
        <el-main v-loading="loading">
            <el-button class="addbotton" @click="addAnnounce" icon="el-icon-plus">
                发布公告
            </el-button>
            <!--            公告面板-->
            <div>
                <el-collapse>
                    <el-collapse-item
                            v-for="announce in announcements"
                            :key="announce.issueDate">
                        <template slot="title">
                            <h2>{{ announce.title }}</h2>
                            &nbsp;&nbsp;&nbsp;&nbsp;
                            <span class="float-right">{{ announce.issueDate }}</span>
                        </template>
                        <div>
                            <el-button type="button" @click="handleEdit(announce)" icon="el-icon-edit">
                                编辑
                            </el-button>
                            <el-button type="button" @click="handleDel(announce)" icon="el-icon-delete">
                                删除
                            </el-button>
                        </div>
                        <div>
                            <pre class="announce-font" >{{ announce.content }}</pre>
                        </div>
                    </el-collapse-item>
                </el-collapse>
            </div>
            <!--发布、编辑公告弹窗-->
            <el-dialog
                    :title="textMap[dialogStatus]"
                    :visible.sync="dialogFormVisible"
                    :lock-scroll="false"
                    top="5%">
                <el-form :model="editForm" label-width="80px" ref="editForm">
                    <el-form-item label="标题">
                        <el-input type="text" v-model="editForm.title"></el-input>
                    </el-form-item>
                    <el-form-item label="内容">
                        <el-input type="textarea" v-model="editForm.content" autosize></el-input>
                    </el-form-item>
                </el-form>
                <span slot="footer" class="el-dialog__footer">
                <el-button @click.native="dialogFormVisible=false">取消</el-button>
                <el-button v-if="dialogStatus==='create'" type="primary" @click="createData">
                    添加
                </el-button>
                <el-button v-else type="primary" @click="updateData">
                    修改
                </el-button>
            </span>
            </el-dialog>
        </el-main>
    </div>
</template>

<script>
    export default {
        name: "TeacherCourseAnnounce",

        data(){
            return{
                loading:true,

                announcements:this.$store.getters.getCourseInfo.notices,

                dialogFormVisible:false,

                dialogStatus: "",

                textMap: {
                    update: "编辑公告",
                    create: "发布公告"
                },

                //发布公告数据
                editForm: {
                    title:"",
                    content:"",
                },
            }
        },

        methods:{
            showAnnounce(){
                this.announcements=this.$store.getters.getCourseInfo.notices;
                this.loading=false;
            },

            // 显示新增页面
            addAnnounce(){
                this.dialogFormVisible = true;
                this.dialogStatus="create";
            },

            // 删除
            handleDel(){
                this.$message.info("公告已删除");
            },

            //显示编辑界面
            handleEdit(announce) {
                this.dialogStatus = "update";
                this.dialogFormVisible = true;
                this.editForm = Object.assign({}, announce);
            },

            // 添加公告
            createData(){
                var that=this;
                this.$http.request({
                    url: '/api/courses/'+this.$store.getters.getCourseId+'/notices/',
                    method: "post",
                    headers: this.$store.getters.authRequestHead,
                    data:{
                        content:this.editForm.content,
                        title:this.editForm.title,
                    }
                })
                    .then(function (response) {
                        console.log(response.data);
                        that.getCourse();
                        that.$message.success("添加公告成功");

                    })
                    .catch(function (error) {
                        console.log(error.response);
                        that.$message.error("添加公告失败："+error.response.data);
                    });

                this.dialogFormVisible=false;
            },

            // 修改课程
            updateData(){
                this.$message.success("公告修改成功");
                this.dialogFormVisible=false;
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
                        that.announcements=response.data.course.notices;
                        this.loading=false;
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
            this.showAnnounce();
        }
    }
</script>

<style scoped>
    .addbotton {
        margin-bottom: 30px;
    }

    .titlesytle {
        text-align: center;
        padding-top: 20px
    }

    .announce-font {
        font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
        white-space: pre-wrap;
        white-space: -moz-pre-wrap;
        /*white-space: -pre-wrap;*/
        white-space: -o-pre-wrap;
        word-wrap: break-word
    }

    .map {
        width: 100%;
        height: 400px;
    }
</style>
