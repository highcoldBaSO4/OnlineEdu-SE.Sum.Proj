<template>
    <el-tag :type="statusTag.type">
        {{ statusTag.text }}
    </el-tag>
</template>

<script>
    export default {
        name: "PaperTag",
        props: {
            status: String
        },
        computed: {
            // statusTagType: function (status) {
            //     if (status === "NOT VIEWED") return "danger";
            //     else if (status === "DOING") return "warning";
            //     else if (status === "SCORING") return "primary";
            //     else if (status === "SUCCESS") return "success";
            // },
            // statusTagText: function (status) {
            //     if (status === "NOT VIEWED") return "未查看";
            //     else if (status === "DOING") return "未完成";
            //     else if (status === "SCORING") return "已完成";
            //     else if (status === "SUCCESS") return "已批改";
            // },
            statusTag: function () {
                switch (this.status) {
                    case "NOT_START": return {
                        type: "danger",
                        text: "未开始"
                    };
                    case "NOT_FINISH": return {
                        type: "warning",
                        text: "未提交",
                    };
                    case "FINISHED": return {
                        type: "primary",
                        text: "已提交"
                    };
                    case "NOT_MARK": return {
                        type: "info",
                        text: "未批改"
                    };
                    case "MARKED": return {
                        type: "success",
                        text: "已批改"
                    };
                    default: return {
                        type: "danger",
                        text: "什么鬼"
                    }
                }
            }
        }
    }
</script>

<style scoped>

</style>
