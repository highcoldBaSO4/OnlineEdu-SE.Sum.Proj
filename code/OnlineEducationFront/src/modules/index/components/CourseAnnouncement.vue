<template>
    <el-collapse>
        <el-collapse-item
                v-for="announce in announcements"
                :key="announce.time">
            <template slot="title">
                <h2>{{ announce.title }}</h2>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <span class="float-right">{{ announce.issueDate }}</span>
            </template>
            <div class="content-style">
                <pre>{{ announce.content }}</pre>
            </div>
        </el-collapse-item>
    </el-collapse>
</template>

<script>
    export default {
        name: "CourseAnnouncement",
        props: {
            announcements: Array,
        },
        mounted() {
            this.announcements.sort((a,b) => {
                if (a.issueDate < b.issueDate) return 1;
                else return -1;
            })
        }
    }
</script>

<style scoped>


</style>
