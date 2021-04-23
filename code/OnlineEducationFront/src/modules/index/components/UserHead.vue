<template>
    <div class="head-style">
        <el-avatar :size="80" :src="avatarUrl" fit="fill"></el-avatar>
        <h2>{{ $store.state.user.username }}</h2>
    </div>
</template>

<script>
    export default {
        name: "UserHead",
        data() {
            return {
                sno: "Stark"
            }
        },
        computed: {
            avatarUrl() {
                console.log(this.$store.getters.userAvatarUrl);
                return this.$store.getters.userAvatarUrl;
            }
        }
    }
</script>

<style scoped>
    .head-style {
        text-align: center;
        padding-top: 30px;
    }
</style>
