<template>
    <div id="ManageNav">
        <el-row class="tac">
            <el-col :span="16">
                <h3>管理</h3>
                <el-menu
                        class="el-menu-vertical-demo"
                        router="true">
                    <el-menu-item index="1" route="/ManageUser">
                        <i class="el-icon-menu"></i>
                        <span slot="title">
                            用户管理
                        </span>
                    </el-menu-item>
                    <el-menu-item index="2" route="/ManageCourse">
                        <i class="el-icon-copy-document"></i>
                        <span slot="title">
                            课程管理
                        </span>
                    </el-menu-item>
                    <el-menu-item index="3" route="/ManageCoursePrototype">
                        <i class="el-icon-c-scale-to-original"></i>
                        <span slot="title">
                            课程原型
                        </span>
                    </el-menu-item>
                </el-menu>
            </el-col>
        </el-row>
    </div>
</template>

<script>
    export default {
        name: "ManageNav",

    }
</script>

<style scoped>
    h3 {
        text-align: center;
    }
</style>
