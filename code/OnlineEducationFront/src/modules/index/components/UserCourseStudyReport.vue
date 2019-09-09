<template>
    <div class="report-div" ref="studyReport">
        <h2>
            我的学习报告
        </h2>
        <p>
            我的成绩：<strong class="score-text">{{scoreText}}</strong>
        </p>
        <div class="study-chart" ref="studychart"></div>
        <el-row>
            <el-col :span="8">
                专注度：{{ concentrationText }} / 100
            </el-col>
            <el-col :span="8">
                努力度：{{ hardworkingText }} / 100
            </el-col>
            <el-col :span="8">
                总学习时长：{{ totalStudyTimeText }}
            </el-col>
        </el-row>
        <el-divider></el-divider>
        <h3>
            我的论坛词云图：
        </h3>
        <img :src="wordMap">
    </div>
</template>

<script>
    import echarts from "echarts";

    export default {
        name: "UserCourseStudyReport",
        //components: {ECharts},
        data() {
            return {
                score: -1,
                concentration: -1,
                hardworking: -1,
                totalStudyTime: -1,
                dailyStudyTime: [],
                wordMap: "",
                reportLoaded: false,
                wordMapLoaded: false,
                scoreLoaded: false,
                chart: null
            }
        },
        methods: {
            addDate: function (date) {
                let temp = new Date(date);
                temp.setDate(temp.getDate() + 1);
                return this.$root.dateToString(temp);
            },
            initChart: function () {
                let dateList = [];
                let timeList = [];
                let sourceList = this.dailyStudyTime;
                console.log(sourceList);
                let startDate = this.$store.getters.getCourseInfo.startDate.substr(0, 10);
                //dateList.push(startDate);
                let current = this.$root.dateToString(new Date());
                let courseEndDate = this.$store.getters.getCourseInfo.endDate.substr(0, 10);
                let endDate = current < courseEndDate ? current : courseEndDate;
                let scanDate = startDate;
                while (scanDate <= endDate) {
                    dateList.push(scanDate);
                    if (sourceList.length !== 0 && sourceList[0].date === scanDate) {
                        timeList.push(sourceList[0].minute);
                        sourceList.shift();
                    }
                    else {
                        timeList.push(0);
                    }
                    scanDate = this.addDate(scanDate);
                }
                console.log(dateList);
                console.log(timeList);
                //let chart = echarts.init(document.getElementById("studychart"));
                console.log(this.chart);
                this.chart.setOption({
                    title: {
                        text: "我的学习情况",
                        left: "center"
                    },
                    xAxis: {
                        type: "category",
                        data: dateList,
                        name: "日期"
                    },
                    yAxis: {
                        type: "value",
                        name: "日学习时间",
                        min: 0,
                        max: function(value) {
                            return Math.ceil(value.max / 60) * 60
                        },
                        maxInterval: 60,
                        axisLabel: {
                            formatter: function (value, index) {
                                return `${(value/60).toFixed(1)}h`
                            }
                        }
                    },
                    series: [
                        {
                            data: timeList,
                            type: "line",
                        }
                    ],
                    color: ['rgba(0,100,155,1)'],
                    dataZoom: [
                        {
                            type: "inside",
                            start: 10,
                            end: 60
                        },
                        {
                            type: "slider"
                        }
                    ],
                    tooltip: {
                        formatter: function (params) {
                            return `${params.name}：共学习 ${(params.value / 60).toFixed(1)}h`
                        },
                        triggerOn: "click"
                    }
                })
            },
            initReport: function () {
                let loading = this.$loading({
                    target: this.$refs["studyReport"],
                    text: "生成报告中",
                    fullscreen: false
                });
                let courseId = this.$store.getters.getCourseId;
                let authHeader = this.$store.getters.authRequestHead;
                this.$http.request({
                    url: `/api/courses/${courseId}/studyRecord/report`,
                    method: "get",
                    headers: authHeader
                }).then((response) => {
                    this.concentration = response.data.report.concentration;
                    this.hardworking = response.data.report.hardworking;
                    this.totalStudyTime = response.data.report.studyTime;
                    this.dailyStudyTime = response.data.studyTimes;
                    this.initChart();
                    loading.close();
                }).catch((error) => {
                    loading.close();
                    this.$root.error(error);
                    //console.error(error);
                    //console.log(error.response);
                });
                this.$http.request({
                    url: `/api/users/${this.$store.state.user.userInfo.id}/courses/${courseId}/forums/`,
                    method: "post",
                    headers: authHeader
                }).then((response) => {
                    this.wordMap = `http://202.120.40.8:30382/online-edu/static/${response.data}?a=${Date()}`;
                    console.log(response);
                }).catch((error) => {
                    //loading.close();
                    console.log(error.response);
                });
                this.$http.request({
                    url: `/api/courses/${courseId}/score`,
                    method: "get",
                    headers: authHeader
                }).then((response) => {
                    //loading.close();
                    this.score = response.data;
                }).catch((error) => {
                    //loading.close();
                    this.$root.error(error);
                    console.log(error);
                })
            }
        },
        computed: {
            scoreText: function () {
                if (this.score === -1) return "成绩还没出呢，再等等吧";
                else return this.score;
            },
            concentrationText: function () {
                if (this.concentration === -1) {
                    return "？？？";
                }
                else {
                    return this.concentration;
                }
            },
            hardworkingText: function () {
                if (this.hardworking === -1) {
                    return "？？？";
                }
                else {
                    return this.hardworking;
                }
            },
            totalStudyTimeText: function () {
                if (this.totalStudyTime === -1) {
                    return "？？？";
                }
                else {
                    return `${Math.trunc(this.totalStudyTime / 60)} h ${this.totalStudyTime % 60} min`;
                }
            }
        },
        mounted() {
            console.log(echarts);
            this.chart = echarts.init(this.$refs["studychart"]);
            this.initReport();
        }
    }
</script>

<style scoped>
    .report-div {
        width: 950px;
        margin-left: auto;
        margin-right: auto;
    }

    .score-text {
        font-size: 20px;
        color: red;
    }

    .study-chart {
        width: 100%;
        margin-left: auto;
        margin-right: auto;
        height: 500px
    }
</style>
