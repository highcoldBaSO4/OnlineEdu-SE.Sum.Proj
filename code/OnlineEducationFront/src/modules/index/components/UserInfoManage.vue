<template>
    <div class="info-form">
        <el-form label-position="left" label-width="100px" :model="userInfo">
            <el-form-item label="真实姓名" prop="realName">
                <el-input v-model="userInfo.realName"></el-input>
            </el-form-item>
            <el-form-item label="手机号" v-if="type === 'modify'">
                <el-input v-model="userInfo.tel"></el-input>
            </el-form-item>
            <el-form-item label="性别">
                <el-radio-group v-model="userInfo.sex">
                    <el-radio label="男">男</el-radio>
                    <el-radio label="女">女</el-radio>
                </el-radio-group>
            </el-form-item>
            <el-form-item label="学校">
                <el-input v-model="userInfo.university"></el-input>
            </el-form-item>
            <el-form-item label="学号">
                <el-input v-model="userInfo.sno"></el-input>
            </el-form-item>
            <el-form-item label="年级">
                <el-select v-model="userInfo.grade">
                    <el-option
                            v-for="grade in gradeList"
                            :key="grade.value"
                            :value="grade.value"
                            :label="grade.label"
                    ></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="专业">
                <el-input v-model="userInfo.major"></el-input>
            </el-form-item>
            <el-form-item>
                <div :class="loading ? 'bottom-buttons-loading' : 'bottom-buttons'">
                    <div class="float-left">
                        <el-button @click="submitInfo" :loading="loading">提交</el-button>
                    </div>
                    <div v-if="type==='register'" class="float-left">
                        <el-button @click="backUp">返回</el-button>
                    </div>
                    <div class="float-right">
                        <el-button @click="resetInfo">重置</el-button>
                    </div>
                </div>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
    export default {
        name: "UserInfoManage",
        props: {
            userdata: Object,
            type: {
                default: "modify"
            },
            loading: {
                default: false
            }
        },
        data() {
            return {
                userInfo: {
                    realName: "",
                    sex: "",
                    tel: "",
                    university: "",
                    sno: "",
                    major: "",
                    grade: 1,
                    email: ""
                },
                cacheInfo: {
                    realName: "",
                    sex: "",
                    tel: "",
                    university: "",
                    sno: "",
                    major: "",
                    grade: 1,
                    email: ""
                },
                gradeList: [
                    {
                        value: 1,
                        label: "大一"
                    },
                    {
                        value: 2,
                        label: "大二"
                    },
                    {
                        value: 3,
                        label: "大三"
                    },
                    {
                        value: 4,
                        label: "大四"
                    },
                    {
                        value: 5,
                        label: "研一"
                    },
                    {
                        value: 6,
                        label: "研二"
                    }
                ]
            }
        },
        methods: {
            submitInfo: function () {
                this.$emit('submit-info', this.userInfo);
            },
            backUp: function () {
                this.$emit('on-back');
            },
            uploadInfo: function(newInfo) {
                this.userInfo = newInfo;
            },
            resetInfo: function () {
                this.userInfo = JSON.parse(JSON.stringify(this.userdata));
            }
        },
        mounted() {
            this.userInfo = JSON.parse(JSON.stringify(this.userdata));
        }
    }
</script>

<style scoped>
    .info-form {
        width: 500px
    }

    .bottom-buttons {
        width: 210px;
        margin-left: auto;
        margin-right: auto;
    }

    .bottom-buttons-loading {
        width: 230px;
        margin-left: auto;
        margin-right: auto;
    }
</style>
