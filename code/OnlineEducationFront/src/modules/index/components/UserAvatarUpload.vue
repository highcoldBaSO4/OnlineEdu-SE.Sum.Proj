<template>
    <el-upload
            class="avatar-uploader avatar"
            :action="uploadUrl"
            :headers="uploadHeader"
            :show-file-list="false"
            :before-upload="beforeAvatarUpload"
            :http-request="uploadProcess"
    >
        <div ref="avatarUpload">
            <el-avatar v-if="imageUrl" :src="imageUrl" class="avatar" />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </div>
    </el-upload>
</template>

<script>
    export default {
        name: "UserAvatarUpload",
        data() {
            return {
                newUrl: ""
            };
        },
        methods: {
            beforeAvatarUpload: function(file) {
                console.log(file);
                const isLt2M = file.size / 1024 < 500;

                if (!isLt2M) {
                    this.$message.error('上传头像图片大小不能超过 500KB!');
                }
                return isLt2M;
            },
            uploadProcess: function(param) {
                let loading = this.$loading({
                    target: this.$refs["avatarUpload"],
                });
                let formData = new FormData();
                console.log(param.file);
                formData.append("avatar",param.file);
                console.log(formData);
                this.$http.request({
                    url: this.uploadUrl,
                    method: "post",
                    data: formData,
                    headers: this.uploadHeader
                }).then((response) => {
                    console.log(response);
                    alert("修改成功");
                    this.newUrl = response.data;
                    this.$store.commit("setNewAvatar", this.newUrl);
                    loading.close();
                }).catch((error) => {
                    alert("修改失败");
                    console.log(error.response);
                    loading.close();
                })
            },
            // uploadSucceed: function(response,file,filelist) {
            //     alert("上传成功");
            //     this.newUrl = response.data;
            //     alert(this.newUrl);
            // },
            // uploadFail: function(error,file,filelist) {
            //     alert("上传失败");
            //     console.log(error);
            // }
        },
        computed: {
            uploadUrl: function () {
                return "/api/users/" + this.$store.state.user.userInfo.id + "/avatar"
            },
            imageUrl: function () {
                if (this.newUrl === "") {
                    return this.$store.getters.userAvatarUrl;
                }
                else {
                    return "http://202.120.40.8:30382/online-edu/static/" + this.newUrl
                }
            },
            uploadHeader: function () {
                return {
                    'Authorization': "Bearer " + this.$store.state.user.accessToken,
                    'Content-Type': 'multipart/form-data'
                }
            }
        }
    }
</script>

<style scoped>
    .avatar-uploader {
        border: 1px dashed #d9d9d9;
        border-radius: 100%;
        cursor: pointer;
        position: relative;
        overflow: hidden;
    }
    .avatar-uploader:hover {
        border-color: #409EFF;
    }
    .avatar-uploader-icon {
        font-size: 28px;
        color: #8c939d;
        width: 178px;
        height: 178px;
        line-height: 178px;
        text-align: center;
    }
    .avatar {
        width: 178px;
        height: 178px;
        display: block;
        object-fit: cover;
    }
</style>
