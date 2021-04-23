import Vue from 'vue'
import Manage from './Manage.vue'
import router from './router'
import store from '../index/store/store.js'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import axios from 'axios'
import message from "element-ui/packages/message/src/main";

axios.defaults.withCredentials = true;
axios.defaults.baseURL = "/online-edu";
Vue.use(ElementUI);
Vue.prototype.$axios=axios;
axios.defaults.baseURL = "/online-edu";

new Vue({
    router,
    store,
    message,
    render: h => h(Manage)
}).$mount('#manage');
