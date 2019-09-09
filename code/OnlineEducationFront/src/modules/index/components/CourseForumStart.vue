<template>
    <el-card>
        <div slot="header">
            <strong>{{ forumTopic.title }}</strong>
            <div class="float-right">
                <UserUnit class="float-left" size="middle" :user-id="forumTopic.userId"></UserUnit>&nbsp;&nbsp;&nbsp;&nbsp;
                {{ forumTopic.createdAt }}
            </div>
            <el-button style="float:right; margin-right: 20px;"
                       @click="banForum(forumTopic.id)"
                       v-if="isCourseTeacher && !forumTopic.locked"
                       type="danger">
                封贴
            </el-button>
        </div>
        <div>
            <pre>{{ forumTopic.content }}</pre>
            <div v-for="image in forumTopic.imageUrls" :key="image">
                <el-image v-if="image"
                     :src="'http://202.120.40.8:30382/online-edu/static/' + image + '?a=' + Math.random()"
                          class="avatar">
                </el-image>
            </div>
        </div>
        <div class="float-right">
            <AddForumResponse :sec-no="forumTopic.secNo" :path="forumTopic.path"></AddForumResponse>
        </div>
        <div class="float-right">
            <el-switch v-model="showResponse" active-text="显示回复" active-color="#13ce66"></el-switch>
        </div>
        <div class="float-clear"></div>
        <div v-if="showResponse">
            <el-divider>回复</el-divider>
            <CourseForumResponse
                    v-for="response in forumTopic.responses"
                    :key="response.createdAt"
                    :response="response"
            ></CourseForumResponse>
        </div>
    </el-card>
</template>

<script>
    import CourseForumResponse from "./CourseForumResponse";
    import AddForumResponse from "./AddForumResponse"
    import UserUnit from "./UserUnit";
    import { mapGetters } from "vuex";

    export default {
        name: "CourseForumStart",
        components: {AddForumResponse, UserUnit, CourseForumResponse},
        props: {
            forumTopic: Object
        },
        data() {
            return {
                showResponse: false,
            }
        },
        methods: {
            banForum(id){
                alert("确定要封贴吗？");

                var that=this;
                this.$http.request({
                    url: '/api/forums/'+id,
                    method: "delete",
                    headers:this.$store.getters.authRequestHead,
                })
                    .then(function (response) {
                        console.log(response.data);
                        that.$root.success("封贴成功");
                    })
                    .catch(function (error) {
                        console.log(error.response);
                        that.$root.error("封贴失败："+error.response.data);
                    });
            }
        },

        computed: {
            ...mapGetters([
                'isCourseTeacher',
            ]),
        }
    }
</script>

<style scoped>

</style>
