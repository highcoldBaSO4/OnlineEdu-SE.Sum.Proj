<template>
    <el-card>
        <div slot="header">
            <div class="float-left">
                <div class="float-left">
                    <UserUnit size="middle" :user-id="response.userId"></UserUnit>
                </div>
                <div class="float-left response-text">回复</div>
                <div class="float-left">
                    <UserUnit size="middle" :user-id="response.responseTo"></UserUnit>
                </div>
            </div>
            <div class="float-right">{{ response.createdAt }}</div>
            <div class="float-clear"></div>
        </div>
        <pre>{{ response.content }}</pre>
        <div v-for="image in response.imageUrls" :key="image">
            <el-image v-if="image"
                      :src="'http://202.120.40.8:30382/online-edu/static/' + image + '?a=' + Math.random()"
                      class="avatar">
            </el-image>
        </div>
        <div class="float-right">
            <AddForumResponse :sec-no="response.secNo" :path="response.path"></AddForumResponse>
        </div>
        <div class="float-right" v-if="response.responses.length !== 0">
            <el-switch v-model="showResponse" active-text="显示回复" active-color="#13ce66"></el-switch>
        </div>
        <div class="float-clear"></div>
        <div v-if="showResponse">
            <el-divider>回复</el-divider>
            <CourseForumResponse
                    v-for="res in response.responses"
                    :key="res.createdAt"
                    :response="res"
            ></CourseForumResponse>
        </div>
    </el-card>
</template>

<script>
    import UserUnit from "./UserUnit";
    import AddForumResponse from "./AddForumResponse"

    export default {
        name: "CourseForumResponse",
        components: {UserUnit, AddForumResponse},
        props: {
            response: Object
        },
        data() {
            return {
                showResponse: false
            }
        }
    }
</script>

<style scoped>
    .response-text {
        padding-left: 20px;
        padding-right: 20px
    }
</style>
