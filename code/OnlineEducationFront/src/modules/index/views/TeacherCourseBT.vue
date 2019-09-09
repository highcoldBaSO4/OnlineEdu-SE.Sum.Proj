<template>
    <div>
        <el-header>
            <h1 class="titlesytle">课程资源</h1>
        </el-header>
        <el-main>
            <div class="float-left">
                <el-upload
                        class="upload-demo"
                        ref="upload"
                        action=""
                        :http-request="uploadBT"
                        :before-upload="beforeUpload"
                        :on-preview="handlePreview"
                        :on-remove="handleRemove"
                        :limit="3"
                        :on-exceed="handleExceed"
                        :auto-upload="false"
                        :file-list="fileList">
                    <el-button slot="trigger" size="small" type="primary" style="margin-right: 10px">点击选择文件</el-button>
                    <el-button size="small" type="success" @click="submitUpload">点击上传</el-button>
                    <div slot="tip" class="el-upload__tip">上传课程资源，一次最多三个</div>
                </el-upload>
                <el-progress v-if="isShowProgress===true" :percentage="BTpercent"></el-progress>
            </div>
<!--            展示所有资源-->
            <el-table :data="courseRes"
                      highlight-current-row="true"
                      v-loading="loading">
                <el-table-column >
                    <el-table-column type="index">
                    </el-table-column>
                    <el-table-column
                            prop="title"
                            label="资源名称"
                            min-width="35%"
                            sortable>
                    </el-table-column>
                    <el-table-column
                            prop="resourceType"
                            label="资源类型"
                            min-width="25%"
                            sortable>
                    </el-table-column>
                    <el-table-column
                            prop="id"
                            label="操作"
                            fixed="right"
                            min-width="20%">
                        <template slot-scope="scope">
                            <el-button type="danger" size="small" @click="handleDel(scope.$index, scope.row)">
                                删除
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
        name: "TeacherCourseBT",

        data(){
            return{
                fileList: [],

                BTtype:"",

                loading:true,

                courseRes:[],

                isShowProgress:false,

                BTpercent:0,
            }
        },

        methods: {
            showAllBT(){
                this.courseRes=this.$store.getters.getCourseInfo.coursePrototype.resources;
                this.loading=false;
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
                let type = file.name.split('.');

                if (type[type.length-1] === 'pdf') {
                    this.BTtype='pdf';
                    return file;
                }
                else if(type[type.length-1]==='ppt' || type[1]==='pptx'){
                    this.BTtype='ppt';
                    return file;
                }
                else if(type[type.length-1]==='mp4' || type[type.length-1]==='avi' || type[type.length-1]==='rmvb'){
                    this.BTtype='video';
                    return file;
                }
                // else if(type[1]==='apk'){
                //     this.BTtype='compression'
                // }
                else{
                    this.BTtype='compression';
                    return file;
                }
            },

            // 上传文件
            uploadBT(file){
                // let type = file.name.split('.');
                //
                // if (type[1] === 'pdf') {
                //     this.BTtype==='pdf';
                // }
                // else if(type[1]==='ppt' || type[1]==='pptx'){
                //     return file
                // }

                this.$message.info("正在上传文件");

                let res = new FormData();
                res.append('resource',file.file);

                var that=this;
                this.$http.request(
                    {
                        url: '/api/coursePrototypes/'+this.$store.getters.getCourseInfo.coursePrototype.id+"/"+this.BTtype+"/",
                        method: "post",
                        headers: {Authorization: "Bearer " + this.$store.state.user.accessToken ,'Content-Type':'multipart/form-data'},
                        data:res,
                        onUploadProgress: (event) => {
                            // 监听上传进度
                            if (event.lengthComputable) {
                                let val = (event.loaded / event.total * 100).toFixed(0);
                                that.isShowProgress = true;
                                that.BTpercent = parseInt(val);
                                console.log(val);
                            }
                        }
                    },
                )
                    .then(function (response) {
                        console.log(response.data);

                        that.getCourse();
                        that.$message.success("上传资源成功");
                        that.isShowProgress=false;

                    })
                    .catch(function (error) {
                        console.log(error);
                        that.$message.error("上传资源失败："+error.response.data);
                    });
            },

        //    删除资源
            handleDel(index, row){
                var that=this;
                this.$http.request(
                    {
                        url: '/api/coursePrototypes/'+this.$store.getters.getCourseInfo.coursePrototype.id+"/"+row.url,
                        method: "delete",
                        headers: {Authorization: "Bearer " + this.$store.state.user.accessToken ,'Content-Type':'multipart/form-data'},
                    },
                )
                    .then(function (response) {
                        console.log(response.data);

                        that.getCourse();
                        that.$message.info("资源已删除");
                        that.isShowProgress=false;

                    })
                    .catch(function (error) {
                        console.log(error);
                        that.$message.error("删除资源失败："+error.response.data);
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
                        that.courseRes=response.data.course.coursePrototype.resources;;
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
            this.showAllBT();
        }
    }
</script>

<style scoped>
    .titlesytle {
        text-align: center;
        padding-top: 20px
    }
</style>
