<template>
    <div>
        <el-steps :active="active" finish-status="success">
            <el-step title="课程原型"></el-step>
            <el-step title="创建课程"></el-step>
        </el-steps>
<!--        申请使用课程原型或创建-->
        <div v-if="active===0">
            <el-table :data="ProtoTable"  height="300px">
                <el-table-column type="index" width="80%">
                    <template slot-scope="scope">
                        <el-radio v-model="ProtoChoose" :label="scope.row.id">&nbsp;</el-radio>
                    </template>
                </el-table-column>
                <el-table-column property="title" label="原型名称" sortable></el-table-column>
                <el-table-column property="description" label="描述"></el-table-column>
                <el-table-column
                        prop="ableToUse"
                        label="使用权限"
                        sortable>
                    <template slot-scope="scope">
                        <el-tag :type="scope.row.tempUse">
                            {{scope.row.ableToUse}}
                        </el-tag>
                    </template>
                </el-table-column>
            </el-table>
            <span slot="footer">
                <el-button type="primary" @click="showCreateProtoDialog">创建课程原型</el-button>
                <el-button @click="applyProto">申请使用该原型</el-button>
                <el-button type="primary" @click="chooseProto">选择该原型</el-button>
            </span>
            <!--课程原型新增页面弹窗-->
            <el-dialog :title="'创建课程原型'"
                       :visible.sync="dialogFormVisible"
                       :lock-scroll="false"
                       top="5%">
                <!--            课程原型的基本信息-->
                <el-form :model="protoForm" label-width="80px" ref="editForm">
                    <el-form-item label="原型名">
                        <el-input type="text" v-model="protoForm.title"></el-input>
                    </el-form-item>
                    <el-form-item label="描述">
                        <el-input type="text" v-model="protoForm.description"></el-input>
                    </el-form-item>
                </el-form>
                <span slot="footer">
                    <el-button @click.native="dialogFormVisible=false">取消</el-button>
                    <el-button type="primary" @click="createProto">
                        创建
                    </el-button>
                </span>
            </el-dialog>
        </div>
<!--        申请开课-->
        <div v-if="active===1">
            <el-form :model="courseForm" label-width="80px" ref="courseForm">
                <el-form-item label="课程名">
                    <el-input type="text" v-model="courseForm.courseTitle"></el-input>
                </el-form-item>
                <el-form-item label="上课日程">
                    <el-col :span="11">
                        <el-date-picker placeholder="选择开始时间"
                                        type="date"
                                        v-model="courseForm.startDate"
                                        value-format="yyyy-MM-dd HH:mm:ss"
                                        style="width: 100%;">
                        </el-date-picker>
                    </el-col>
                    <el-col class="line" :span="2">-</el-col>
                    <el-col :span="11">
                        <el-date-picker placeholder="选择结束时间"
                                        type="date"
                                        v-model="courseForm.endDate"
                                        value-format="yyyy-MM-dd HH:mm:ss"
                                        style="width: 100%;">
                        </el-date-picker>
                    </el-col>
                </el-form-item>
                <!--                课程的上课时间段-->
                <el-form-item label="课时">
                    <el-form-item
                            v-for="(timeslot, index) in courseForm.timeSlots"
                            :label="'时间段'+(index+1)"
                            :key="timeslot.day"
                            :rules="{required: true, message: '内容不能为空', trigger: 'blur'}">
<!--                        <el-input v-model="timeslot.day" placeholder="输入0-6，0代表周日"></el-input>-->
                        <el-select v-model="timeslot.day" placeholder="请选择">
                            <el-option
                                    v-for="item in options"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value">
                            </el-option>
                        </el-select>
                        <el-time-select
                                v-model="timeslot.start"
                                :picker-options="{selectableRange: '8:00:00 - 20:30:00'}"
                                placeholder="选择开始时间">
                        </el-time-select>
                        <el-time-select
                                v-model="timeslot.end"
                                :picker-options="{selectableRange: '8:00:00 - 20:30:00'}"
                                placeholder="选择结束时间">
                        </el-time-select>
                        <el-button @click.prevent="removeTimeslot(timeslot)">删除</el-button>
                    </el-form-item>
                </el-form-item>
                <el-form-item>
                    <el-button @click="addTimeslot">新增时间段</el-button>
                </el-form-item>
                <el-form-item label="上课地点">
                    <el-input type="text" v-model="courseForm.location"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer">
                <el-button type="primary" @click="createCourse">创建课程</el-button>
            </span>
        </div>
    </div>
</template>

<script>

    export default {
        name: "TeacherCreateCourse",

        data() {
            return {
                active: 0,

                ProtoTable:[],

                ProtoChoose:"",

                dialogFormVisible:false,

                protoForm: {
                    title: "",
                    description:"",
                },

                courseForm: {
                    id:0,
                    courseTitle:"",
                    startDate:"",
                    endDate:"",
                    location:"",
                    courseTeacher:"",
                    courseState:"",
                    state:"",
                    timeSlots:[
                        {
                            day:0,
                            end:"",
                            start:"",
                        },
                    ]
                },

                options: [{
                    value: '0',
                    label: '周日'
                }, {
                    value: '1',
                    label: '周一'
                }, {
                    value: '2',
                    label: '周二'
                }, {
                    value: '3',
                    label: '周三'
                }, {
                    value: '4',
                    label: '周四'
                },{
                    value: '5',
                    label: '周五'
                }, {
                    value: '6',
                    label: '周六'
                },
                ],
            }
        },

        methods:{
            // 展示可用课程原型
            showAllProtos(){
                //显示所有课程原型
                var that=this;
                that.ProtoTable=[];

                // alert("第一步");

                this.$http.request({
                    url: '/api/coursePrototypes/info/all',
                    method: "get",
                    headers:this.$store.getters.authRequestHead,
                })
                    .then(function (response) {
                        console.log(response.data);
                        let xx=0;

                        for (let x=0; x<response.data.length; x++){
                            //可使用的课程原型展示出来
                            if (response.data[x].state==='USING'){
                                that.ProtoTable.push(response.data[x]);

                                let tempTeacher=response.data[x].users;
                                that.ProtoTable[xx].ableToUse=false;
                                that.ProtoTable[xx].tempUse='danger';
                                var tempId=that.$store.getters.getUserId;

                                //看该教师是否能使用该课程原型
                                for (let y=0; y<tempTeacher.length; y++) {
                                    if (tempTeacher[y].id===tempId){
                                        that.ProtoTable[xx].ableToUse=true;
                                        that.ProtoTable[xx].tempUse='success';
                                    }
                                }
                                xx++;
                            }
                        }
                        console.log(that.ProtoTable);
                    })
                    .catch(function (error) {
                        console.log(error.response);
                        alert("请求失败");
                    });
            },

            // 显示创建课程原型页面
            showCreateProtoDialog(){
                this.dialogFormVisible=true;
                this.protoForm={
                    id:"",
                    title: "",
                    description:"",
                };
            },

            // 创建课程原型
            createProto(){
                var that=this;

                this.$http.request({
                    url: '/api/coursePrototypes/',
                    method: "post",
                    headers:this.$store.getters.authRequestHead,
                    data:{
                        title:this.protoForm.title,
                        description:this.protoForm.description,
                    }
                })
                    .then(function (response) {
                        console.log(response.data);

                        that.showAllProtos();
                        that.dialogFormVisible=false;
                    })
                    .catch(function (error) {
                        console.log(error);
                        // alert("请求失败");
                    });
            },

            // 申请使用课程原型
            applyProto(){
                var that=this;

                this.$http.request({
                    url: '/api/coursePrototypes/'+that.ProtoChoose+'/apply',
                    method: "post",
                    headers:this.$store.getters.authRequestHead,
                })
                    .then(function (response) {
                        console.log(response.data);

                        that.$message.info("正在申请");

                        that.showAllProtos();
                        that.dialogFormVisible=false;
                    })
                    .catch(function (error) {
                        console.log(error);
                        // alert("请求失败");
                    });
            },

            // 选择课程原型
            chooseProto(){
                // console.log(this.ProtoChoose);
                for(let x=0; x<this.ProtoTable.length; x++) {
                    if(this.ProtoTable[x].id===this.ProtoChoose) {
                        if (this.ProtoTable[x].ableToUse===true) {
                            this.active++;
                        }
                        else {
                            this.$message.warning("您不能使用该课程原型！");
                        }
                    }
                }
            },

            // 创建课程
            createCourse(){
                var that=this;

                this.$http.request({
                    url: '/api/courses/start',
                    method: "post",
                    headers: this.$store.getters.authRequestHead,
                    params:{
                        prototypeId:this.ProtoChoose,
                    },
                    data:{
                        courseTitle:this.courseForm.courseTitle,
                        startDate:this.courseForm.startDate,
                        endDate:this.courseForm.endDate,
                        location:this.courseForm.location,
                        timeSlots:this.courseForm.timeSlots,
                    }
                })
                    .then(function (response) {
                        console.log(response.data);
                        that.$router.push('/user');
                        // alert("请求成功");
                    })
                    .catch(function (error) {
                        console.log(error);
                        // alert("请求失败");
                    });

            },

            // 移除时间段
            removeTimeslot(item) {
                var index = this.courseForm.timeSlots.indexOf(item);
                if (index !== -1) {
                    this.courseForm.timeSlots.splice(index, 1)
                }
            },

            // 添加时间段
            addTimeslot() {
                this.courseForm.timeSlots.push({
                    day:'',
                    start:'',
                    end:'',
                });
            },
        },

        mounted() {
            this.showAllProtos();
        }
    }
</script>

<style scoped>

</style>
