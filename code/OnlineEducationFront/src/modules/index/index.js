import Vue from 'vue'
import Index from './Index.vue'
import router from './router'
import store from './store/store.js'
import ElementUI from 'element-ui'
import axios from 'axios'
import VueAxios from 'vue-cli-plugin-axios'
import 'element-ui/lib/theme-chalk/index.css';
import '../../assets/div-layout.css'
import '../../assets/icon/iconfont.css'
import BaiduMap from 'vue-baidu-map';
import message from "element-ui/packages/message/src/main";

Vue.use(BaiduMap, {
    // ak 是在百度地图开发者平台申请的密钥
    ak: 'tZ8u8pMf9nhjWFRACvTHN3gDOZCXcGxx'
});

axios.defaults.withCredentials = true;
axios.defaults.baseURL = "/online-edu";
Vue.config.productionTip = false;
Vue.use(ElementUI);
Vue.use(VueAxios, axios);
Vue.prototype.$http = axios;
//Vue.prototype.$record = new RecordBehavior();

let transTime = (time) => {
    return time < 10 ? `0${time}` : time
};

new Vue({
    router,
    store,
    message,
    methods: {
        error: function (errorText) {
            this.$message({
                type: "error",
                message: errorText,
                showClose: true
            })
        },
        success: function (successText) {
            this.$message({
                type: "success",
                message: successText,
                showClose: true
            })
        },
        dateToString: function(date, detail = false) {
            let currentTime = date;
            let yyyy = currentTime.getFullYear();
            let MM = transTime(currentTime.getMonth() + 1);
            let dd = transTime(currentTime.getDate());
            let res = `${yyyy}-${MM}-${dd}`;
            if (detail) {
                let HH = transTime(currentTime.getHours());
                let mm = transTime(currentTime.getMinutes());
                let ss = transTime(currentTime.getSeconds());
                res = `${res} ${HH}:${mm}:${ss}`;
            }
            return res;
        },
        recordBehavior: function(action, info) {
            let currentTime = new Date();
            let record = {
                action,
                time: this.dateToString(currentTime, true),
                info
            };
            console.log(record);
            this.$http.request({
                url: `/api/courses/${this.$store.getters.getCourseId}/studyRecord/submit`,
                method: "post",
                data: record,
                headers: this.$store.getters.authRequestHead
            }).then((response) => {
                console.log(response);
            }).catch((error) => {
                console.log(error.response)
            });
        },
        recordVideoBehavior: function (action, videoName, videoTime, extraInfo) {
            let videoInfo = {
                videoName,
                videoTime
            };
            Object.assign(videoInfo, extraInfo);
            this.recordBehavior(action, videoInfo);
        },
        recordStartPlay: function(videoName, videoTime) {
            this.recordVideoBehavior("START_PLAY", videoName, videoTime)
        },
        recordPause: function(videoName, videoTime){
            this.recordVideoBehavior("PAUSE", videoName, videoTime);
        },
        recordContinue: function(videoName, videoTime){
            this.recordVideoBehavior("CONTINUE", videoName, videoTime);
        },
        recordChangeSpeed: function(videoName, videoTime, newSpeed){
            this.recordVideoBehavior("CHANGE_SPEED", videoName, videoTime, {
                newSpeed
            });
        },
        recordFinishPlay: function(videoName, videoTime){
            this.recordVideoBehavior("FINISH_WATCH", videoName, videoTime);
        },
        recordJump: function (videoName, videoTime) {
            this.recordVideoBehavior("JUMP", videoName, videoTime);
        }
    },
    created() {
        this.$http.interceptors.response.use((response) => {
            return response;
        }, (error) => {
            if (error.response.status === 401 && error.response.statusText === "Unauthorized" && error.response.data.path !== "/online-edu/api/auth/signin") {
                this.error("未登录或登录失效，请先登录");
                this.$router.push("login");
                return Promise.reject(error);
            }
            else {
                return Promise.reject(error);
            }
        })
    },
    render: h => h(Index)
}).$mount('#index');
