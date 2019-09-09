<template>
    <div>
        <el-header>
            <h1 class="titlesytle">课程题库</h1>
        </el-header>
        <el-main v-loading="loading">
<!--            作业显示部分-->
            <div>
                <el-button @click="showSingleDialog">添加单选题</el-button>
                <el-button @click="showMultiDialog">添加多选题</el-button>
                <el-button @click="showJudgeDialog">添加判断题</el-button>
                <el-button @click="showSubDialog">添加主观题</el-button>
<!--                所有题目展示-->
                <div v-for="question in questions" :key="question.id">
                    <el-divider></el-divider>
<!--                    <div style="float: right">-->
<!--                        <el-button size="small" @click="showQuestionEdit(question)">编辑</el-button>-->
<!--                        <el-button size="small" type="danger" @click="deleteQuestion">删除</el-button>-->
<!--                    </div>-->
                    <div v-if="question.questionType==='SINGLE_ANSWER'|| question.questionType==='MULTIPLE_ANSWER'">
                        <h4>
                            Question: {{question.content}}
                        </h4>
                        <div v-for="(choice,count) in question.options" :key="choice">
                            【{{count}}】 {{choice}}
                        </div>
                    </div>
                    <div v-else>
                        <h4>
                            Question: {{question.question}}
                        </h4>
                    </div>
<!--                    展示图片-->
                    <div v-for="image in question.images" :key="image">
                        <el-image v-if="image"
                             :src="'http://202.120.40.8:30382/online-edu/static/' + image + '?a=' + Math.random()"
                                  class="avatar"></el-image>
                    </div>
                    <div style="color:red" v-if="question.questionType!=='SUBJECTIVE'">
                        <p>Answer: {{question.answer}}</p>
                    </div>
                </div>
            </div>
<!--            添加单选题弹窗部分-->
            <el-dialog :title="'单选题'"
                       :visible.sync="singleVisible"
                       top="5%">
                <el-form :model="singleEditForm" label-width="80px" ref="singleEditForm">
                    <el-form-item label="题目">
                        <el-input type="textarea" v-model="singleEditForm.title"></el-input>
                    </el-form-item>
                    <el-form-item
                            v-for="(choice, index) in singleEditForm.choices"
                            :label="'选项'+(index+1)"
                            :key="choice.key"
                            :prop="'choice.' + index + '.content'"
                            :rules="{required: true, message: '内容不能为空', trigger: 'blur'}">
                        <el-input v-model="choice.content"></el-input><el-button @click.prevent="removeChoiceSingle(choice)">删除</el-button>
                    </el-form-item>
                    <el-form-item>
                        <el-button @click="addChoiceSingle">新增选项</el-button>
                    </el-form-item>
                    <el-form-item label="答案">
                        <el-input v-model="singleEditForm.correctAnswer" placeholder="请输入大写字母ABCD……"></el-input>
                    </el-form-item>
<!--                    增加图片-->
                    <el-form-item prop="file" ref="uploadElement">
                        <el-upload ref="upload"
                                   action="#"
                                   :multiple="true"
                                   list-type="picture-card"
                                   :auto-upload="false"
                                   :http-request="UploadImg"
                                   accept="image/png,image/gif,image/jpg,image/jpeg">
                            <i class="el-icon-plus"></i>
                        </el-upload>
                    </el-form-item>
                </el-form>
                <span slot="footer" class="el-dialog__footer">
                    <el-button @click.native="singleVisible=false">取消</el-button>
                    <el-button type="primary" @click="createSingleData">添加</el-button>
                </span>
            </el-dialog>
<!--            添加多选题弹窗部分-->
            <el-dialog :title="'多选题'"
                       :visible.sync="multiVisible"
                       top="5%">
                <el-form :model="multiEditForm" label-width="80px" ref="multiEditForm">
                    <el-form-item label="题目">
                        <el-input type="textarea" v-model="multiEditForm.title"></el-input>
                    </el-form-item>
                    <el-form-item
                            v-for="(choice, index) in multiEditForm.choices"
                            :label="'选项'+(index+1)"
                            :key="choice.key"
                            :prop="'choice.' + index + '.content'"
                            :rules="{required: true, message: '内容不能为空', trigger: 'blur'}">
                        <el-input v-model="choice.content"></el-input><el-button @click.prevent="removeChoiceMulti(choice)">删除</el-button>
                    </el-form-item>
                    <el-form-item>
                        <el-button @click="addChoiceMulti">新增选项</el-button>
                    </el-form-item>
                    <el-form-item label="答案">
                        <el-input v-model="multiEditForm.correctAnswer" placeholder="请输入大写字母ABCD……"></el-input>
                    </el-form-item>
                    <!--                    增加图片-->
                    <el-form-item prop="file" ref="uploadElement">
                        <el-upload ref="upload"
                                   action="#"
                                   :multiple="true"
                                   list-type="picture-card"
                                   :auto-upload="false"
                                   :http-request="UploadImg"
                                   accept="image/png,image/gif,image/jpg,image/jpeg">
                            <i class="el-icon-plus"></i>
                        </el-upload>
                    </el-form-item>
                </el-form>
                <span slot="footer" class="el-dialog__footer">
                    <el-button @click.native="multiVisible=false">取消</el-button>
                    <el-button type="primary" @click="createMultiData">添加</el-button>
                </span>
            </el-dialog>
<!--            添加判断题弹窗部分-->
            <el-dialog :title="'判断题'"
                       :visible.sync="judgeVisible"
                       top="5%">
                <el-form :model="judgeEditForm" label-width="80px" ref="judgeEditForm">
                    <el-form-item label="题目">
                        <el-input type="textarea" v-model="judgeEditForm.title"></el-input>
                    </el-form-item>
                    <el-form-item label="答案">
                        <el-input v-model="judgeEditForm.correctAnswer" placeholder="请输入正确或错误"></el-input>
                    </el-form-item>
                    <!--                    增加图片-->
                    <el-form-item prop="file" ref="uploadElement">
                        <el-upload ref="upload"
                                   action="#"
                                   :multiple="true"
                                   list-type="picture-card"
                                   :auto-upload="false"
                                   :http-request="UploadImg"
                                   accept="image/png,image/gif,image/jpg,image/jpeg">
                            <i class="el-icon-plus"></i>
                        </el-upload>
                    </el-form-item>
                </el-form>
                <span slot="footer" class="el-dialog__footer">
                    <el-button @click.native="judgeVisible=false">取消</el-button>
                    <el-button type="primary" @click="createJudgeData">添加</el-button>
                </span>
            </el-dialog>
<!--            添加主观题弹窗部分-->
            <el-dialog :title="'主观题'"
                       :visible.sync="subVisible"
                       top="5%">
                <el-form :model="subEditForm" label-width="80px" ref="subEditForm">
                    <el-form-item label="题目">
                        <el-input type="textarea" v-model="subEditForm.title"></el-input>
                    </el-form-item>
                    <!--                    增加图片-->
                    <el-form-item prop="file" ref="uploadElement">
                        <el-upload ref="upload"
                                   action="#"
                                   :multiple="true"
                                   list-type="picture-card"
                                   :auto-upload="false"
                                   :http-request="UploadImg"
                                   accept="image/png,image/gif,image/jpg,image/jpeg">
                            <i class="el-icon-plus"></i>
                        </el-upload>
                    </el-form-item>
                </el-form>
                <span slot="footer" class="el-dialog__footer">
                    <el-button @click.native="subVisible=false">取消</el-button>
                    <el-button type="primary" @click="createSubData">添加</el-button>
                </span>
            </el-dialog>
        </el-main>
    </div>
</template>

<script>
    export default {
        name: "TeacherCourseOneAssignment",

        data(){
            return{
                loading:true,

                singleVisible: false,

                multiVisible: false,

                judgeVisible: false,

                subVisible: false,

                singleEditForm:{
                    key:"",
                    title:"",
                    choices:[
                        {
                            tag:"",
                            content:"",
                        }
                    ],
                    correctAnswer:'',
                    images:"",
                },

                multiEditForm:{
                    key:"",
                    title:"",
                    choices:[
                        {
                            tag:"",
                            content:"",
                        }
                    ],
                    correctAnswer:'',
                },

                judgeEditForm:{
                    key:"",
                    title:"",
                    correctAnswer:"",
                },

                subEditForm:{
                    key:"",
                    title:"",
                },

                questions:[],

                imageURL:"",

                formData: new FormData(),

            }
        },

        methods:{
            showAllQuestions(){
                // this.$http.request({
                //     url: this.$store.getters.getCourseUrl + "info",
                //     method: "get",
                //     headers: this.$store.getters.authRequestHead
                // }).then((response) => {
                //     console.log(response.data);
                //
                // }).catch((error) => {
                //     alert(error);
                //     console.log(error.response);
                // });
                this.questions=this.$store.getters.getCourseInfo.coursePrototype.questions;
                this.loading=false;
            },

            // 显示各种题型的新建dialog
            showSingleDialog(){
                this.singleVisible=true;
                this.singleEditForm={
                    key:"",
                        title:"",
                        choices:[
                        {
                            tag:"",
                            content:"",
                        }
                    ],
                    correctAnswer:'',
                    images: '',
                }
            },

            showMultiDialog(){
                this.multiVisible=true;
                this.multiEditForm={
                    key:"",
                    title:"",
                    choices:[
                        {
                            tag:"",
                            content:"",
                        }
                    ],
                    correctAnswer:'',
                }
            },

            showJudgeDialog(){
                this.judgeVisible=true;
                this.judgeEditForm={
                    key:"",
                    title:"",
                    correctAnswer:'',
                }
            },

            showSubDialog(){
                this.subVisible=true;
                this.subEditForm={
                    key:"",
                    title:"",
                }
            },

            showQuestionEdit(question){
                if (question.questionType==='SINGLE_ANSWER'){
                    this.showSingleEditDialog(question);
                }
            },

            // 显示各种题型的编辑dialog
            showSingleEditDialog(single){
                this.singleVisible=true;
                this.singleEditForm = Object.assign({}, single);
            },

            showMultiEditDialog(multi){
                this.multiVisible=true;
                this.multiEditForm = Object.assign({}, multi);
            },

            showJudgeEditDialog(judge){
                this.judgeVisible=true;
                this.judgeEditForm = Object.assign({}, judge);
            },

            showSubEditDialog(sub){
                this.subVisible=true;
                this.subEditForm = Object.assign({}, sub);
            },

            // 新建各种题型
            createSingleData(){
                var that=this;
                that.formData = new FormData();
                that.$refs.upload.submit();
                var newQuestionId=0;

                var singleChoices=[];
                for (let x=0; x<that.singleEditForm.choices.length; x++){
                    singleChoices.push(that.singleEditForm.choices[x].content);
                }

                // 先上传题目
                this.$http.request({
                    url: '/api/coursePrototypes/'+this.$store.getters.getCourseInfo.coursePrototype.id+'/questions/submit',
                    method: "post",
                    headers:{
                        'Authorization': "Bearer " + this.$store.state.user.accessToken,
                    },
                    data:{
                        type:"single_answer",
                        content:this.singleEditForm.title,
                        options:singleChoices,
                        answer:this.singleEditForm.correctAnswer,
                    },
                })
                    .then(function (response) {
                        console.log(response.data);
                        newQuestionId=response.data.id;
                        if(newQuestionId!==0)
                        {
                            that.$message.info("上传图片中");
                            // 再上传图片
                            that.$http.request({
                                url: '/api/coursePrototypes/'+that.$store.getters.getCourseInfo.coursePrototype.id+'/questions/'+newQuestionId,
                                method: "post",
                                headers:{
                                    'Authorization': "Bearer " + that.$store.state.user.accessToken,
                                    'Content-Type': 'multipart/form-data'
                                },
                                data:that.formData,
                            })
                                .then(function (res) {
                                    console.log(res.data);
                                    // alert("上传单选题成功");
                                })
                                .catch(function (error2) {
                                    console.log(error2.response);
                                    // alert("请求失败");
                                });
                        }
                        that.getCourse();
                        that.$message.success("添加单选题成功");
                    })
                    .catch(function (error) {
                        console.log(error.response);
                        that.$message.error("添加单选题失败："+error.response.data);
                    });
                this.singleVisible=false;
            },

            createMultiData(){
                var that=this;
                that.formData = new FormData();
                that.$refs.upload.submit();
                var newQuestionId=0;

                var multiChoices=[];
                for (let x=0; x<that.multiEditForm.choices.length; x++){
                    multiChoices.push(that.multiEditForm.choices[x].content);
                }

                this.$http.request({
                    url: '/api/coursePrototypes/'+this.$store.getters.getCourseInfo.coursePrototype.id+'/questions/submit',
                    method: "post",
                    headers:this.$store.getters.authRequestHead,
                    data:{
                        type:"multiple_answer",
                        content:this.multiEditForm.title,
                        options:multiChoices,
                        answer:this.multiEditForm.correctAnswer,
                    },
                })
                    .then(function (response) {
                        console.log(response.data);
                        newQuestionId=response.data.id;
                        if(newQuestionId!==0)
                        {
                            that.$message.info("上传图片中");
                            // 再上传图片
                            that.$http.request({
                                url: '/api/coursePrototypes/'+that.$store.getters.getCourseInfo.coursePrototype.id+'/questions/'+newQuestionId,
                                method: "post",
                                headers:{
                                    'Authorization': "Bearer " + that.$store.state.user.accessToken,
                                    'Content-Type': 'multipart/form-data'
                                },
                                data:that.formData,
                            })
                                .then(function (res) {
                                    console.log(res.data);
                                    // alert("上传多选题成功");
                                })
                                .catch(function (error2) {
                                    console.log(error2.response);
                                    // alert("请求失败");
                                });
                        }
                        that.getCourse();
                        that.$message.success("添加多选题成功");
                    })
                    .catch(function (error) {
                        console.log(error.response);
                        that.$message.error("添加多选题失败："+error.response.data);
                    });
                this.multiVisible=false;
            },

            createJudgeData(){
                var that=this;
                that.formData = new FormData();
                that.$refs.upload.submit();
                var newQuestionId=0;
                this.$http.request({
                    url: '/api/coursePrototypes/'+this.$store.getters.getCourseInfo.coursePrototype.id+'/questions/submit',
                    method: "post",
                    headers:this.$store.getters.authRequestHead,
                    data:{
                        type:"t_or_f",
                        content:this.judgeEditForm.title,
                        answer:this.judgeEditForm.correctAnswer,
                    },
                })
                    .then(function (response) {
                        console.log(response.data);
                        newQuestionId=response.data.id;
                        if(newQuestionId!==0)
                        {
                            that.$message.info("上传图片中");
                            // 再上传图片
                            that.$http.request({
                                url: '/api/coursePrototypes/'+that.$store.getters.getCourseInfo.coursePrototype.id+'/questions/'+newQuestionId,
                                method: "post",
                                headers:{
                                    'Authorization': "Bearer " + that.$store.state.user.accessToken,
                                    'Content-Type': 'multipart/form-data'
                                },
                                data:that.formData,
                            })
                                .then(function (res) {
                                    console.log(res.data);
                                    // alert("上传判断题成功");
                                })
                                .catch(function (error2) {
                                    console.log(error2.response);
                                    // alert("请求失败");
                                });
                        }
                        that.getCourse();
                        that.$message.success("添加判断题成功");
                    })
                    .catch(function (error) {
                        console.log(error.response);
                        that.$message.error("添加判断题失败："+error.response.data);
                    });
                this.judgeVisible=false;
            },

            createSubData(){
                var that=this;
                that.formData = new FormData();
                that.$refs.upload.submit();
                var newQuestionId=0;
                this.$http.request({
                    url: '/api/coursePrototypes/'+this.$store.getters.getCourseInfo.coursePrototype.id+'/questions/submit',
                    method: "post",
                    headers:this.$store.getters.authRequestHead,
                    data:{
                        type:"subjective",
                        content:this.subEditForm.title,
                    },
                })
                    .then(function (response) {
                        console.log(response.data);
                        newQuestionId=response.data.id;
                        if(newQuestionId!==0)
                        {
                            that.$message.info("上传图片中");
                            // 再上传图片
                            that.$http.request({
                                url: '/api/coursePrototypes/'+that.$store.getters.getCourseInfo.coursePrototype.id+'/questions/'+newQuestionId,
                                method: "post",
                                headers:{
                                    'Authorization': "Bearer " + that.$store.state.user.accessToken,
                                    'Content-Type': 'multipart/form-data'
                                },
                                data:that.formData,
                            })
                                .then(function (res) {
                                    console.log(res.data);
                                    // alert("上传多选题成功");
                                })
                                .catch(function (error2) {
                                    console.log(error2.response);
                                    // alert("请求失败");
                                });
                        }
                        that.getCourse();
                        that.$message.success("添加主观题成功");
                    })
                    .catch(function (error) {
                        console.log(error.response);
                        that.$message.error("添加主观题失败："+error.response.data);
                    });
                this.subVisible=false;
            },

            // 删除题目
            deleteQuestion(){
                this.$message.info("删除成功");
            },

            // 单选和多选移除选项
            removeChoiceSingle(item) {
                var index = this.singleEditForm.choices.indexOf(item)
                if (index !== -1) {
                    this.singleEditForm.choices.splice(index, 1)
                }
            },

            removeChoiceMulti(item) {
                var index = this.multiEditForm.choices.indexOf(item)
                if (index !== -1) {
                    this.multiEditForm.choices.splice(index, 1)
                }
            },

            // 单选和多选添加选项
            addChoiceSingle() {
                this.singleEditForm.choices.push({
                    content: '',
                    tag:'',
                });
            },

            addChoiceMulti() {
                this.multiEditForm.choices.push({
                    content: '',
                    tag:'',
                });
            },

            UploadImg (file) {
                this.formData.append('file', file.file);
            },

            // 移除照片
            handleRemove(file, fileList) {
                console.log(file, fileList);
            },

            // 预览照片
            // handlePictureCardPreview(file) {
            //     this.dialogImageUrl = file.url;
            //     this.dialogVisible = true;
            // },

            getCourse(){
                var that=this;
                this.$http.request({
                    url: '/api/courses/'+this.$store.getters.getCourseId+'/info',
                    method: "get",
                    headers: this.$store.getters.authRequestHead,
                })
                    .then(function (response) {
                        console.log(response.data);
                        that.questions=response.data.course.coursePrototype.questions;
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
            this.showAllQuestions();

        }
    }
</script>

<style scoped>
   .titlesytle {
        text-align: center;
        padding-top: 20px
    }

    .avatar {
        width: 178px;
        height: 178px;
        display: block;
    }
</style>
