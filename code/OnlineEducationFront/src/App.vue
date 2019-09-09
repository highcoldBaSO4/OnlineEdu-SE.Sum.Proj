<template>
  <div id="app">
    <el-container>
      <el-header height="75px">
        <MainMenu></MainMenu>
      </el-header>
      <el-main>
        <div class="main-body"><router-view/></div>
      </el-main>
    </el-container>
  </div>
</template>

<style>
#app {
  /*font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;*/
}

  .main-body {
    width: 1350px;
    margin-left: auto;
    margin-right: auto;
    border-style: solid;
  }
</style>
<script>
  import MainMenu from "./components/MainMenu.vue";
  export default {
    components: {MainMenu}
  }
</script>