<template>
    <el-table :data="papers">
        <el-table-column prop="title" label="标题"></el-table-column>
        <el-table-column prop="start" label="开始时间"></el-table-column>
        <el-table-column prop="end" label="结束时间"></el-table-column>
        <el-table-column prop="status" label="状态">
            <template slot-scope="scope">
                <el-tag :type="statusTagType(scope.row.status)">
                    {{ statusTagText(scope.row.status) }}
                </el-tag>
            </template>
        </el-table-column>
        <el-table-column label="链接">
            <template scope="scope">
                <el-link @click="loadPaperPage(scope.$index, scope.row)">进入作业</el-link>
            </template>
        </el-table-column>
    </el-table>
</template>

<script>
    export default {
        name: "UserCoursePaperList",
        data() {
            return {
                papers: [
                    // {
                    //     id: 0,
                    //     title: "第一次作业",
                    //     start: "2019-07-15",
                    //     end: "2019-07-25",
                    //     status: "NOT VIEWED"
                    // }
                ]
            }
        },
        methods: {
            statusTagType: function (status) {
                if (status === "NOT_START") return "danger";
                else if (status === "NOT_FINISH" || status==="TEMP_SAVE") return "warning";
                else if (status === "FINISHED") return "primary";
                else if (status === "MARKED") return "success";
                else if (status === "NOT_MARKED") return "info";
            },
            statusTagText: function (status) {
                if (status === "NOT_START") return "未查看";
                else if (status === "NOT_FINISH") return "未完成";
                else if (status === "TEMP_SAVE") return "暂存";
                else if (status === "FINISHED") return "已完成";
                else if (status === "NOT_MARKED") return "未批改";
                else if (status === "MARKED") return "已批改";
            },

            showAllPapers(){
                var that=this;
                var allPaperData=[];
                this.$http.request({
                    url: '/api/courses/'+this.$store.getters.getCourseId+'/papers/state',
                    method: "get",
                    headers: this.$store.getters.authRequestHead,
                })
                    .then(function (response) {
                        console.log(response.data);
                        allPaperData=response.data;

                        for (var x=0; x<allPaperData.length; x++){
                            var tempPaper={
                                id: allPaperData[x].paper.id,
                                title: allPaperData[x].paper.title,
                                start: allPaperData[x].paper.start,
                                end: allPaperData[x].paper.end,
                                status: allPaperData[x].state,
                            };
                            that.papers.push(tempPaper);
                        }

                    })
                    .catch(function (error) {
                        console.log(error.response);
                        alert("请求失败");
                    });
            },

            // 加载作业页面
            loadPaperPage: function (index, row) {
                this.$router.push({
                    name: "courseStudentPaper",
                    params: {
                        paperId: row.id
                    }
                });
            }
        },

        mounted() {
            this.showAllPapers();
        }
    }
</script>

<style scoped>

</style>
