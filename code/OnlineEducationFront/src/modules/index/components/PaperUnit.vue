<template>
    <div class="paper-unit float-left">
        <el-link @click="loadPaperPage">
            <i class="iconfont el-icon-my-homework"></i>
            {{ paperInfo.title }}
        </el-link>
    </div>
</template>

<script>
    export default {
        name: "PaperUnit",
        props: {
            paperInfo: {
                id: 0,
                state: "",
                title: ""
            }
        },
        methods: {
            loadPaperPage: function () {
                this.$router.push({
                    name: "courseStudentPaper",
                    params: {
                        paperId: this.paperInfo.id
                    }
                });
            }
        },
        computed: {
            badgeType: function () {
                switch (this.paperInfo.state) {
                    case "NOT START":
                        return "danger";
                    case "NOT FINISH":
                        return "warning";
                    case "FINISHED":
                        return "primary";
                    case "NOT MARKED":
                        return "info";
                    case "MARKED":
                        return "success";
                    default:
                        return "danger";
                }
            },
            badgeText: function () {
                switch (this.paperInfo.state) {
                    case "NOT START":
                        return "未开始";
                    case "NOT FINISH":
                        return "未完成";
                    case "FINISHED":
                        return "已完成";
                    case "NOT MARKED":
                        return "未评分";
                    case "MARKED":
                        return "已评分";
                    default:
                        return "什么鬼";
                }
            },
        }
    }
</script>

<style scoped>
    .paper-unit {
        width: 120px;
        padding-left: 20px;
        padding-right: 20px;
        text-align: center;
    }
</style>
