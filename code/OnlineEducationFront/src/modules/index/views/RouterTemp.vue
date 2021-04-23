<template>
    <router-view></router-view>
</template>

<script>
    export default {
        name: "RouterTemp",
        mounted() {
            let loading = this.$loading({
                fullscreen: true,
                text: "加载课程"
            });
            this.$http.request({
                url: this.$store.getters.getCourseUrl + "info",
                method: "get",
                headers: this.$store.getters.authRequestHead
            }).then((response) => {
                console.log(response.data);
                let identity = response.data.identity;
                this.$store.commit("setCourseInfo", response.data.course);
                this.$store.commit("setIdentity", identity);
                console.log(this.$store.getters.getCourseInfo);
                loading.close();
                if (this.$route.path === "/course") {
                    if (identity === "VISITOR") {
                        this.$router.push('/course/info');
                    }
                    else if (identity === "STUDENT") {
                        this.$router.push('/course/student');
                    }
                    else {
                        this.$router.push('/course/manager');
                    }
                }
            }).catch((error) => {
                if (error.response.status === 401) {
                    this.$root.error("请先登录");
                }
                else this.$message.error(error);
                console.log(error.response);
                loading.close();
            })
        }
    }
</script>

<style scoped>

</style>
