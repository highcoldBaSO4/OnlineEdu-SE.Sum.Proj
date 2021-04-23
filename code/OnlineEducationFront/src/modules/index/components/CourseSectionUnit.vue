<template>
    <div>
        <el-collapse-item>
            <h3 slot="title">{{ sectionInfo.title }}&nbsp;&nbsp;&nbsp;&nbsp;</h3>
            <AddNewSection
                    v-if="isCourseTeacher"
                    slot="title"
                    :last-section="sectionInfo.branchNo"
                    :chapterId="chapterId"
            ></AddNewSection>
            <el-divider>章节介绍</el-divider>
            <pre>{{ sectionInfo.description }}</pre>
            <el-divider>章节资源</el-divider>
            <div>
                <el-button
                        v-if="isCourseTeacher"
                        style="float: right"
                        @click="showResourceDialog"
                >添加资源</el-button>
                <ResourceUnit
                        v-for="(resource, index) in sectionInfo.resources"
                        :key="index"
                        :resource-info="resource"
                ></ResourceUnit>
                <div class="float-clear"></div>
            </div>
            <el-divider>章节作业</el-divider>
            <div>
                <el-button style="float: right" @click="showAssignDialog" v-if="isCourseTeacher">添加作业</el-button>
                <PaperUnit
                        v-for="(paper, index) in sectionInfo.papers"
                        :key="index"
                        :paper-info="paper"
                ></PaperUnit>
                <div class="float-clear"></div>
            </div>
            <el-divider></el-divider>
        </el-collapse-item>
<!--        选择资源弹窗-->
        <el-dialog :title="'选择资源'"
                   :visible.sync="resDialogVisible"
                   :lock-scroll="false"
                   top="5%">
            <el-table :data="resTable"  height="300px">
                <el-table-column type="index" width="80%">
                    <template slot-scope="scope">
                        <el-radio v-model="resChoose" :label="scope.row.id">&nbsp;</el-radio>
                    </template>
                </el-table-column>
                <el-table-column property="title" label="资源名称" sortable></el-table-column>
                <el-table-column property="resourceType" label="资源类型"></el-table-column>
            </el-table>
            <span slot="footer">
                <el-button @click.native="resDialogVisible=false">取消</el-button>
                <el-button type="primary" @click="chooseResource">选择</el-button>
            </span>
        </el-dialog>
        <!--        选择作业弹窗-->
        <el-dialog :title="'选择作业'"
                   :visible.sync="assignDialogVisible"
                   :lock-scroll="false"
                   top="5%">
            <el-table :data="assignTable"  height="300px">
                <el-table-column type="index" width="80%">
                    <template slot-scope="scope">
                        <el-radio v-model="assignChoose" :label="scope.row.id">&nbsp;</el-radio>
                    </template>
                </el-table-column>
                <el-table-column property="title" label="作业名称" sortable></el-table-column>
<!--                <el-table-column property="resourceType" label="资源类型"></el-table-column>-->
            </el-table>
            <span slot="footer">
                <el-button @click.native="assignDialogVisible=false">取消</el-button>
                <el-button type="primary" @click="chooseAssign">选择</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    import ResourceUnit from "./ResourceUnit";
    import PaperUnit from "./PaperUnit";
    import AddNewSection from "./AddNewSection";
    import { mapGetters } from "vuex"

    export default {
        name: "CourseSectionUnit",
        components: {AddNewSection, PaperUnit, ResourceUnit},
        props: {
            sectionInfo: {
                branchNo:"",
                title: "",
                description: "",
                resources: [],
                papers: [],
                sectionBranchesPrimaryKey:{
                    branchId:"",
                },
            },
            chapterId: Number,
        },

        data(){
            return{
                resDialogVisible:false,

                assignDialogVisible:false,

                resTable:this.$store.getters.getCourseInfo.coursePrototype.resources,

                assignTable:this.$store.getters.getCourseInfo.papers,

                resChoose:"",

                assignChoose:"",
            }
        },

        methods:{
            // 显示资源对话框
            showResourceDialog(){
                this.resTable=this.$store.getters.getCourseInfo.coursePrototype.resources;
                this.resDialogVisible=true;
            },

            // 显示作业对话框
            showAssignDialog(){
                this.assignTable=this.$store.getters.getCourseInfo.papers;
                this.assignDialogVisible=true;
            },

            // 加入资源
            chooseResource(){
                var that=this;
                this.$http.request({
                    url: '/api/courses/'+this.$store.getters.getCourseId+'/sections/'+this.chapterId+'/'+this.sectionInfo.sectionBranchesPrimaryKey.branchId+'/resources/issue',
                    method: "post",
                    headers: this.$store.getters.authRequestHead,
                    params:{
                        resourceId:this.resChoose,
                    }
                })
                    .then(function (response) {
                        console.log(response.data);
                        that.getCourse();
                        that.$root.success("发布资源成功");
                    })
                    .catch(function (error) {
                        console.log(error);
                        that.$root.error("发布资源失败"+error.response.data);
                    });
                this.resDialogVisible=false;
            },

            // 加入作业
            chooseAssign(){
                var that=this;
                this.$http.request({
                    url: '/api/courses/'+this.$store.getters.getCourseId+'/sections/'+this.chapterId+'/'+this.sectionInfo.sectionBranchesPrimaryKey.branchId+'/papers/issue',
                    method: "post",
                    headers: this.$store.getters.authRequestHead,
                    params:{
                        paperId:this.assignChoose,
                    }
                })
                    .then(function (response) {
                        console.log(response.data);
                        that.getCourse();
                        that.$root.success("发布作业成功");
                    })
                    .catch(function (error) {
                        console.log(error);
                        that.$root.error("发布作业失败"+error.response.data);
                    });
                this.assignDialogVisible=false;
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
                        that.$store.commit("setCourseInfo",response.data.course);
                        that.$forceUpdate();
                        that.loading=false;
                        // that.$store.commit("setCourseInfo",response.data);
                        // alert("请求成功");
                    })
                    .catch(function (error) {
                        console.log(error);
                        // alert("请求失败");
                    });
            }
        },

        computed: {
            ...mapGetters([
                'isCourseTeacher'
            ]),
        }
    }
</script>

<style scoped>

</style>
