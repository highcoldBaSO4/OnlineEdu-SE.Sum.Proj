<template>
<!--    主观题组件-->
    <div id="AssignmentSub">
        <el-card>
            <h4>
                {{sub.content}}
            </h4>
            <el-input type="textarea" v-model="sub.myAnswer" style="width: 80%;"></el-input>
            <!--        上传图片-->
            <p>上传图片</p>
            <el-upload
                    ref="imgUpload"
                    action="#"
                    list-type="picture-card"
                    :http-request="addImg"
                    :auto-upload="false">
                <i slot="default" class="el-icon-plus"></i>
                <div slot="file" slot-scope="{file}">
                    <img class="el-upload-list__item-thumbnail" :src="file.url" alt="">
                    <span class="el-upload-list__item-actions">
                    <span class="el-upload-list__item-preview" @click="handlePictureCardPreview(file)">
                        <i class="el-icon-zoom-in"></i>
                    </span>
                    <span v-if="!disabled" class="el-upload-list__item-delete" @click="handleDownload(file)">
                        <i class="el-icon-download"></i>
                    </span>
                    <span v-if="!disabled" class="el-upload-list__item-delete" @click="handleRemove(file)">
                        <i class="el-icon-delete"></i>
                    </span>
                </span>
                </div>
            </el-upload>
            <!--        上传附件-->
            <p>上传附件</p>
            <el-upload
                    class="upload-demo"
                    ref="fileUpload"
                    action="#"
                    :on-preview="handlePreview"
                    :on-remove="handleRemove"
                    :before-remove="beforeRemove"
                    multiple
                    :limit="3"
                    :on-exceed="handleExceed"
                    :auto-upload="false"
                    :http-request="addFile"
                    :file-list="fileList">
                <el-button size="small" type="primary">点击上传</el-button>
                <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
            </el-upload>
        </el-card>
    </div>
</template>

<script>
    export default {
        name: "AssignmentSub",

        props:{
            sub: Object,
        },

        data() {
            return {
                dialogImageUrl: '',
                dialogVisible: false,
                disabled: false,
                fileList:[],
                imgList:[],
            };
        },

        methods: {
            handleRemove(file) {
                console.log(file);
            },

            handlePictureCardPreview(file) {
                this.dialogImageUrl = file.url;
                this.dialogVisible = true;
            },

            handleDownload(file) {
                console.log(file);
            },

            handlePreview(file) {
                console.log(file);
            },

            handleExceed(files, fileList) {
                this.$message.warning(`当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
            },

            beforeRemove(file, fileList) {
                return this.$confirm(`确定移除 ${ file.name }？`);
            },

            addImg(file) {
                this.imgList.push(file.file);
                console.log(this.imgList);
            },

            addFile(file) {
                this.fileList.push(file.file);
            },

            saveSub() {
                let uploadForm = new FormData();
                this.$refs.imgUpload.submit();
                this.$refs.fileUpload.submit();
                uploadForm.append("images", this.imgList);
                uploadForm.append("file", this.fileList);
                uploadForm.append("answerText", this.sub.myAnswer);
                uploadForm.append("questionId", this.sub.id);
                uploadForm.append("state", "TEMP_SAVE");
                console.log(uploadForm);
                return uploadForm;
            }
        },

        mounted() {

        }
    }
</script>

<style scoped>

</style>
