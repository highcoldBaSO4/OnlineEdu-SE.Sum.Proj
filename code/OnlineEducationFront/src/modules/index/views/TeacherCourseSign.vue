<template>
    <div>
        <el-header>
            <h1 class="titlesytle">课程签到</h1>
        </el-header>
        <el-main>
            <el-button class="addbotton" @click="addSignIn" icon="el-icon-location">
                发布签到
            </el-button>
            <div>
<!--                显示所有签到-->
                <el-collapse>
                    <el-collapse-item
                            v-for="sign in signIns"
                            :key="sign.signInPrimaryKey.signInNo">
                        <template slot="title">
                            <h2>第{{ sign.signInPrimaryKey.signInNo }}次签到</h2>
                            &nbsp;&nbsp;&nbsp;&nbsp;
                            <span class="float-right">{{ sign.startDate }}</span>
                            &nbsp;至&nbsp;&nbsp;&nbsp;&nbsp;
                            <span class="float-right">{{ sign.endDate }}</span>
                        </template>
<!--                        显示签到的学生-->
                        <div>
                            <el-row :gutter="20" v-for="student in sign.users" :key="student.username">
                                <el-col :span="6">
                                    <div class="grid-content">
                                        {{student.username}}
                                    </div>
                                </el-col>
                            </el-row>
<!--                            <div v-for="student in sign.users" :key="student.username">-->
<!--                                {{student.username}}-->
<!--                            </div>-->
                        </div>
                    </el-collapse-item>
                </el-collapse>
            </div>
<!--            发布签到对话框-->
            <el-dialog
                    :title="发布签到"
                    :visible.sync="signDialogVisible"
                    :lock-scroll="false"
                    top="5%">
                <el-form :model="signInForm" label-width="80px" ref="signInForm">
                    <el-form-item label="签到时间">
                        <el-col :span="11">
                            <el-date-picker placeholder="选择开始时间"
                                            type="datetime"
                                            v-model="signInForm.startDate"
                                            value-format="yyyy-MM-dd HH:mm:ss"
                                            style="width: 100%;">
                            </el-date-picker>
                        </el-col>
                        <el-col class="line" :span="2">-</el-col>
                        <el-col :span="11">
                            <el-date-picker placeholder="选择结束时间"
                                            type="datetime"
                                            v-model="signInForm.endDate"
                                            value-format="yyyy-MM-dd HH:mm:ss"
                                            style="width: 100%;">
                            </el-date-picker>
                        </el-col>
                    </el-form-item>
                    <!--                    百度地图控件-->
                    <el-form-item label="选择地点">
                        <div style="width: 100%; height: 100%">
                            <baidu-map class="map"
                                       :center="center"
                                       scroll-wheel-zoom="true"
                                       zoom="15"
                                       @click="getClickInfo"
                                       @ready="handler">
                                <bm-navigation anchor="BMAP_ANCHOR_TOP_RIGHT"></bm-navigation>
                                <bm-geolocation anchor="BMAP_ANCHOR_BOTTOM_RIGHT"
                                                :showAddressBar="true"
                                                :autoLocation="true">
                                </bm-geolocation>
                                <bm-marker :position="{lng: center.lng, lat: center.lat}" :dragging="true" animation="BMAP_ANIMATION_BOUNCE" v-model="signPosition">
                                    <bm-label content="签到地点" :labelStyle="{color: 'black', fontSize : '15px'}" :offset="{width: -35, height: 30}"/>
                                </bm-marker>
                                <bm-city-list anchor="BMAP_ANCHOR_TOP_LEFT"></bm-city-list>
                            </baidu-map>
                        </div>
                    </el-form-item>
                </el-form>
                <span slot="footer" class="el-dialog__footer">
                    <el-button @click.native="signDialogVisible=false">取消</el-button>
                    <el-button type="primary" @click="createSign">
                        发布
                    </el-button>
                </span>
            </el-dialog>
        </el-main>
    </div>
</template>

<script>
    export default {
        name: "TeacherCourseSign",

        data(){
            return{
                signIns:this.$store.getters.getCourseInfo.signIns,

                signInForm:{
                    endDate: "",
                    startDate: "",
                },

                signDialogVisible:false,

                center: {lng: 116.404, lat: 39.915},
            }
        },

        methods:{
            // 显示发布签到dialog
            addSignIn(){
                this.signDialogVisible=true;
            },

            // 发布签到
            createSign(){
                console.log(this.center);

                var that=this;
                this.$http.request({
                    url: '/api/courses/'+this.$store.getters.getCourseId+'/signIns',
                    method: "post",
                    headers: this.$store.getters.authRequestHead,
                    data:{
                        endDate: this.signInForm.endDate,
                        latitude: this.center.lat,
                        longitude: this.center.lng,
                        startDate: this.signInForm.startDate,
                    }
                })
                    .then(function (response) {
                        console.log(response.data);
                        that.getCourse();
                        that.$message.success("发布签到成功");
                        that.signDialogVisible=false;

                    })
                    .catch(function (error) {
                        console.log(error.response);
                        that.$message.error("发布签到失败"+error.response.data);
                    });
            },

            // 点击的地方设为地图中心
            getClickInfo (e) {
                this.center.lng = e.point.lng;
                this.center.lat = e.point.lat;
            },

            handler({ BMap }) {
                // console.log(BMap, map);
                this.tools.analysisAddress(
                    [
                        { lng: 116.307852, lat: 40.057031 },
                        { lng: 116.307852, lat: 40.057031 }
                    ],
                    res => {
                        console.log(res);
                    }
                );
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
                        that.signIns=response.data.course.signIns;
                        that.loading=false;
                        // that.$store.commit("setCourseInfo",response.data);
                        // alert("请求成功");
                    })
                    .catch(function (error) {
                        console.log(error);
                        // alert("请求失败");
                    });
            }

        }
    }
</script>

<style scoped>
    .titlesytle {
        text-align: center;
        padding-top: 20px
    }

    .addbotton {
        margin-bottom: 30px;
    }

    .map {
        width: 100%;
        height: 400px;
    }

    .grid-content {
        border-radius: 4px;
        min-height: 36px;
    }
</style>
