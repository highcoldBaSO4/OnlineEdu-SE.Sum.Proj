<template>
    <div>
        <div class="login-square">
            <el-form>
                <el-form-item>登录</el-form-item>
                <el-form-item>
                    <el-input placeholder="用户名" suffix-icon="el-icon-user"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-input placeholder="密码" suffix-icon="el-icon-lock"></el-input>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script>
    export default {
        name: "Login"
    }
</script>

<style scoped>
    .login-square {
        margin-right: 30px;
    }
</style>