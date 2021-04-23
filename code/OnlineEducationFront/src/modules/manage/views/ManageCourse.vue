<template>
    <div>
        <div>
            <h3>课程管理</h3>
            <div class="divleft">
                <el-input
                        class="padding"
                        v-model="search"
                        placeholder="请输入课程名"
                        prefix-icon="el-icon-search"/>
            </div>
            <div class="divright">
                <el-button @click="handleChooseProto">新增</el-button>
            </div>
            <el-table :data="CourseData.filter(data=>!search || data.courseTitle.includes(search))"
                      class="coursetable"
                      v-loading="loading"
                      highlight-current-row="true">
                <el-table-column >
                    <el-table-column type="index">
                    </el-table-column>
                    <el-table-column
                            prop="courseTitle"
                            label="课程名"
                            min-width="30%"
                            sortable
                    ></el-table-column>
                    <el-table-column
                            prop="startDate"
                            label="开始时间"
                            min-width="30%"
                            sortable
                    ></el-table-column>
                    <el-table-column
                            prop="endDate"
                            label="结束时间"
                            min-width="30%"
                            sortable
                    ></el-table-column>
                    <el-table-column
                            prop="location"
                            label="上课地点"
                            min-width="35%"
                            sortable
                    ></el-table-column>
                    <el-table-column
                            prop="courseTeacher"
                            label="授课教师"
                            min-width="25%"
                            sortable
                    ></el-table-column>
<!--                    显示课程状态的tab-->
                    <el-table-column
                            prop="courseState"
                            label="课程状态"
                            min-width="25%"
                            sortable>
                        <template slot-scope="scope">
                            <el-tag :type="scope.row.courseStateTemp">
                                {{scope.row.courseState}}
                            </el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column
                            prop="id"
                            label="操作"
                            min-width="40%">
                        <template slot-scope="scope">
                            <el-button type="primary" size="small" @click="handleEdit(scope.$index, scope.row)">
                                修改
                            </el-button>
                            <el-button type="danger" size="small" @click="handleDel(scope.$index, scope.row)">
                                删除
                            </el-button>
                        </template>
                    </el-table-column>
                </el-table-column>
            </el-table>
        </div>
<!--        选择课程原型-->
        <el-dialog :title="'选择课程原型'"
                   :visible.sync="dialogProtoVisible"
                   :lock-scroll="false"
                   top="5%">
            <el-table :data="dialogProtoForm"  height="300px">
                <el-table-column type="index" width="80%">
                    <template slot-scope="scope">
                        <el-radio v-model="dialogProtoChoose" :label="scope.row.id">&nbsp;</el-radio>
                    </template>
                </el-table-column>
                <el-table-column property="title" label="原型名称" sortable></el-table-column>
                <el-table-column property="description" label="描述"></el-table-column>
            </el-table>
            <span slot="footer">
                <el-button @click.native="dialogProtoVisible=false">取消</el-button>
                <el-button type="primary" @click="handleAdd">创建课程</el-button>
            </span>
        </el-dialog>
        <!--课程编辑页面弹窗-->
        <el-dialog :title="textMap[dialogStatus]"
                   :visible.sync="dialogFormVisible"
                   :lock-scroll="false"
                   top="5%">
<!--            课程的基本信息-->
            <el-form :model="editForm" label-width="80px" ref="editForm">
                <el-form-item label="课程名">
                    <el-input type="text" v-model="editForm.courseTitle"></el-input>
                </el-form-item>
                <el-form-item label="上课日程">
                    <el-col :span="11">
                        <el-date-picker placeholder="选择开始时间"
                                        type="date"
                                        v-model="editForm.startDate"
                                        value-format="yyyy-MM-dd HH:mm:ss"
                                        style="width: 100%;">
                        </el-date-picker>
                    </el-col>
                    <el-col class="line" :span="2">-</el-col>
                    <el-col :span="11">
                        <el-date-picker placeholder="选择结束时间"
                                        type="date"
                                        v-model="editForm.endDate"
                                        value-format="yyyy-MM-dd HH:mm:ss"
                                        style="width: 100%;">
                        </el-date-picker>
                    </el-col>
                </el-form-item>
<!--                课程的上课时间段-->
                <el-form-item label="课时">
                    <el-form-item
                            v-for="(timeslot, index) in editForm.timeSlots"
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
                                :picker-options="{start: '08:00',step: '00:30',end: '20:30'}"
                                placeholder="选择开始时间">
                        </el-time-select>
                        <el-time-select
                                v-model="timeslot.end"
                                :picker-options="{start: '08:00',step: '00:30',end: '20:30'}"
                                placeholder="选择结束时间">
                        </el-time-select>
                        <el-button @click.prevent="removeTimeslot(timeslot)">删除</el-button>
                    </el-form-item>
                </el-form-item>
                <el-form-item>
                    <el-button @click="addTimeslot">新增时间段</el-button>
                </el-form-item>
                <el-form-item label="上课地点">
                    <el-input type="text" v-model="editForm.location"></el-input>
                </el-form-item>
                <el-form-item label="授课教师" v-if="dialogStatus==='update'">
                    <el-input type="text" v-model="editForm.courseTeacher"></el-input>
                </el-form-item>
<!--                编辑时才显示-->
                <el-form-item label="课程状态"
                              v-if="editForm.courseState!=='进行中' && editForm.courseState!=='已结束' && dialogStatus==='update'">
                    <el-radio-group v-model="editForm.courseState">
                        <el-radio label="待审核"></el-radio>
                        <el-radio label="未开始"></el-radio>
                        <el-radio label="未通过"></el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-form>
<!--            编辑页面下的学生信息，编辑时才显示-->
            <div v-if="dialogStatus==='update'">
<!--                <div class="divleftmargin">-->
<!--                    <el-input type="text"-->
<!--                              placeholder="请输入学生学号"-->
<!--                              v-model="addStudent">-->
<!--                    </el-input>-->
<!--                </div>-->
<!--                <div class="divleft">-->
<!--                    <el-button @click="AddCourseStudent">-->
<!--                        增加学生-->
<!--                    </el-button>-->
<!--                </div>-->
                <el-table :data="StudentForm" height="300px">
                    <el-table-column type="index"></el-table-column>
                    <el-table-column property="sno" label="学号" sortable></el-table-column>
                    <el-table-column property="username" label="学生"></el-table-column>
                    <el-table-column>
                        <template slot-scope="scope">
                            <el-button type="button" @click="DelStudent(scope.$index, scope.row)">
                                删除
                            </el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
            <span slot="footer">
                <el-button @click.native="dialogFormVisible=false">取消</el-button>
                <el-button v-if="dialogStatus==='create'" type="primary" @click="createData">
                    添加
                </el-button>
                <el-button v-else type="primary" @click="updateData">
                    修改
                </el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    import getHeader from "../managerRequestHeader.js";

    export default {
        name: "ManageCourse",

        data(){
            return{
                search:"",

                CourseData: [],

                loading:true,

                dialogFormVisible:false,

                dialogProtoVisible:false,

                dialogStatus: "",

                textMap: {
                    update: "编辑",
                    create: "新增"
                },

                //编辑界面数据
                editForm: {
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

                //课程原型，和选择的课程原型
                dialogProtoForm:[],

                dialogProtoChoose:"",

                addStudent:"",

                // 课程的学生信息
                StudentForm: [],

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
            showAllCourse(){
                var that=this;
                this.$axios.request({
                        url: '/api/courses/all/info',
                        method: "get",
                        headers: getHeader.requestHeader()
                    })
                    .then(function (response) {
                        console.log(response.data);
                        // alert("请求成功");

                        that.CourseData=response.data;
                        that.loading=false;

                        // 管理状态
                        for (let index=0; index<that.CourseData.length; index++)
                        {
                            var tempState;
                            tempState=response.data[index].state;

                            // 处理教师的显示
                            if (response.data[index].teacher!=null)
                            {
                                var tempTeacher;
                                tempTeacher=response.data[index].teacher.username;
                                that.CourseData[index].courseTeacher=tempTeacher;
                            }

                            // 处理状态的显示
                            if (tempState==='APPLYING') {
                                that.CourseData[index].courseStateTemp="warning";
                                that.CourseData[index].courseState="待审核";
                            }
                            else if(tempState==='READY_TO_START') {
                                that.CourseData[index].courseStateTemp = 'primary';
                                that.CourseData[index].courseState='未开始';
                            }
                            else if(tempState==='TEACHING'){
                                that.CourseData[index].courseStateTemp = 'success';
                                that.CourseData[index].courseState='进行中';
                            }
                            else if(tempState==='FINISHED'){
                                that.CourseData[index].courseStateTemp = 'info';
                                that.CourseData[index].courseState='已结束';
                            }
                            else if(tempState==='NOT_PASS'){
                                that.CourseData[index].courseStateTemp = 'danger';
                                that.CourseData[index].courseState='未通过';
                            }
                        }

                    })
                    .catch(function (error) {
                        console.log(error.response);
                        // alert("请求失败");
                    })
            },

            handleDel:function(index,row){
                this.$message.info(row.courseName+"已删除");
            },

            //显示编辑界面
            handleEdit: function(index, row) {
                this.dialogStatus = "update";
                this.editForm = Object.assign({}, row);
                this.editForm.timeSlots=[
                    {
                        day:0,
                        end:"",
                        start:"",
                    },
                ];

                // 该课程所有学生
                var that=this;
                this.$axios.request({
                    url: '/api/courses/'+this.editForm.id+'/students',
                    method: "get",
                    headers: getHeader.requestHeader()
                })
                    .then(function (response) {
                        console.log(response.data);
                        // alert("请求成功");
                        that.StudentForm = response.data;
                    })
                    .catch(function (error) {
                        console.log(error.response);
                        // alert("请求失败");
                    });

                this.dialogFormVisible = true;
            },

            //显示选择课程原型弹窗
            handleChooseProto(){
                //显示所有课程原型
                var that=this;
                that.dialogProtoForm=[];

                this.$axios.request({
                    url: '/api/coursePrototypes/info/all',
                    method: "get",
                    headers: getHeader.requestHeader()
                })
                    .then(function (response) {
                        console.log(response.data);

                        for (let x=0; x<response.data.length; x++){
                            if (response.data[x].state==='USING'){
                                that.dialogProtoForm.push(response.data[x]);
                            }
                        }
                        // console.log(that.dialogProtoForm);
                        // that.dialogProtoForm=response.data;
                    })
                    .catch(function (error) {
                        console.log(error.response);
                        // alert("请求失败");
                        that.$message.error(error.response.data);
                    });

                this.dialogProtoVisible=true;
            },

            //显示新增课程界面
            handleAdd: function() {
                this.dialogProtoVisible=false;
                this.dialogStatus = "create";
                this.editForm = {
                    id:0,
                    courseTitle:"",
                    startDate:"",
                    endDate:"",
                    location:"",
                    courseTeacher:"",
                    courseState:"",
                    state:"",
                    timeSlots:[],
                };
                this.editForm.id=this.dialogProtoChoose;
                // alert(this.editForm.id);
                this.dialogFormVisible = true;
            },

            // 添加课程
            createData(){
                var that=this;

                this.$axios.request({
                    url: '/api/courses/start',
                    method: "post",
                    headers: getHeader.requestHeader(),
                    params:{
                        prototypeId:this.editForm.id,
                    },
                    data:{
                        courseTitle:this.editForm.courseTitle,
                        startDate:this.editForm.startDate,
                        endDate:this.editForm.endDate,
                        location:this.editForm.location,
                        timeSlots:this.editForm.timeSlots
                    }
                })
                    .then(function (response) {
                        console.log(response.data);

                        that.showAllCourse();
                        that.dialogFormVisible=false;
                        that.$message.success("请求成功");
                    })
                    .catch(function (error) {
                        console.log(error);
                        // alert("请求失败");
                        that.$message.error(error.response.data);
                    });

                this.dialogFormVisible=false;
            },

            // 修改课程
            updateData(){
                var that=this;

                if (this.editForm.courseState==='未通过'){
                    this.editForm.state='disapproval';
                }
                else if(this.editForm.courseState==='未开始'){
                    this.editForm.state='approval';
                }

                // 修改权限
                this.$axios.request({
                    url: '/api/courses/'+this.editForm.id+'/start',
                    method: "post",
                    headers: getHeader.requestHeader(),
                    params:{
                        decision:this.editForm.state,
                    }
                })
                    .then(function (response) {
                        console.log(response.data);

                        that.showAllCourse();
                        that.dialogFormVisible=false;
                        that.$message.success("修改权限成功");
                    })
                    .catch(function (error) {
                        console.log(error);
                        // alert("请求失败");
                        that.$message.error(error.response.data);
                    });

                // if (this.editForm.timeSlots.day==='MONDAY')
                // {
                //     this.editForm.timeSlots.day='1';
                // }
                //编辑课程信息
                this.$axios.request({
                    url: '/api/courses/'+this.editForm.id+'/modify',
                    method: "put",
                    headers: getHeader.requestHeader(),
                    data:{
                        courseTitle:this.editForm.courseTitle,
                        startDate:this.editForm.startDate,
                        endDate:this.editForm.endDate,
                        location:this.editForm.location,
                        timeSlots:this.editForm.timeSlots,
                    }
                })
                    .then(function (response) {
                        console.log(response.data);

                        that.showAllCourse();
                        that.dialogFormVisible=false;
                        that.$message.success("修改课程信息成功");
                    })
                    .catch(function (error) {
                        console.log(error);
                        // alert("请求失败");
                        that.$message.error(error.response.data);
                    });
            },

            //课程添加学生
            AddCourseStudent(){
                this.$message.success(this.addStudent+" "+"该课程添加学生成功");
            },

            //课程删除学生
            DelStudent: function(index, row){
                this.$message.info(row.username+"已删除");
            },

            // 移除时间段
            removeTimeslot(item) {
                var index = this.editForm.timeSlots.indexOf(item);
                if (index !== -1) {
                    this.editForm.timeSlots.splice(index, 1)
                }
            },

            // 添加时间段
            addTimeslot() {
                this.editForm.timeSlots.push({
                    day:'',
                    start:'',
                    end:'',
                });
            },

        },

        mounted() {
            this.showAllCourse();
        }
    }
</script>

<style>
    .divleft {
        float: left;
    }

    .divleftmargin{
        float: left;
        margin-right: 20px;
    }

    .divright {
        float: right;
    }

    .padding {
        padding: 8px;
    }

    .coursetable {
        width: 100%;
        font-size: 15px;
        padding-bottom:20px;
    }

</style>
