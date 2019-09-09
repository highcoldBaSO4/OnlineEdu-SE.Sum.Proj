import Vue from 'vue'
import './plugins/axios'
import App from './App.vue'
import './plugins/element.js'
import router from './router'
import store from './store'
import message from "element-ui/packages/message/src/main";

Vue.config.productionTip = false

new Vue({
  router,
  store,
  message,
  render: h => h(App)
}).$mount('#app')
