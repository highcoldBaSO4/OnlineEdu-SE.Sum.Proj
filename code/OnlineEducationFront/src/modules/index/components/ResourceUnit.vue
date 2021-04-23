<template>
    <div class="resource-unit float-left">
        <el-link @click="getResource">
            <i :class="symbolIcon"></i>
            {{ resourceInfo.title }}
        </el-link>

    </div>
</template>

<script>
    export default {
        name: "ResourceUnit",
        props: {
            resourceInfo: {
                resourceType: String,
                url: String,
                title: String
            }
        },
        data() {
            return {
                showVideo: false
            }
        },
        methods: {
            getResource: function () {
                if (this.resourceInfo.resourceType === "VIDEO") {
                    alert("haha");
                    this.$router.push({
                        name: "courseStudentVideo",
                        params: {
                            videoUrl: this.resourceInfo.url,
                            videoTitle: this.resourceInfo.title
                        }
                    })
                }
                else {
                    window.location.href = this.getFullUrl(this.resourceInfo.url);
                }
            },
            getFullUrl: function (resourceUrl) {
                return "http://202.120.40.8:30382/online-edu/static/" + resourceUrl;
            }
        },
        computed: {
            symbolIcon: function () {
                return "iconfont el-icon-my-" + this.resourceInfo.resourceType.toLowerCase();
            }
        }
    }
</script>

<style scoped>
    .resource-unit {
        width: 120px;
        padding-left: 20px;
        padding-right: 20px;
        text-align: center;
    }
</style>
