import axios from 'axios'

 axios.defaults.withCredentials = true;

const state = {
    username: "",
    accessToken: "",
    loginStatus: false,
    userInfo: {
        assistCourses: [],
        learnCourses: [],
        teachCourses: [],
        email: "",
        grade: 0,
        major: "",
        realName: "",
        sex: "",
        sno: "",
        id:"",
        tno: "",
        tel: "",
        university: "",
        avatarUrl: "",
        roles: [
            {
                id: 0,
                role: "",
            }
        ]
    }
};

// getters
const getters = {
    authRequestHead: state => {
        return {
            Authorization: "Bearer " + state.accessToken
        }
    },
    userAvatarUrl: state => {
        if (state.userInfo.avatarUrl !== "") {
            return "http://202.120.40.8:30382/online-edu/static/" + state.userInfo.avatarUrl + "?a=" + Math.random();
        }
        else return "";
    },
    userRole: state => {
        let transRoles = [];
        for (let role of state.userInfo.roles) {
            transRoles.push(role.role);
        }
        return transRoles;
    },
    assistCourses: state => {
        return state.userInfo.assistCourses;
    },
    learnCourses: state => {
        return state.userInfo.learnCourses;
    },
    teachCourses: state => {
        return state.userInfo.teachCourses;
    },
    isStudent: function () {
        //let transRoles = [];
        for (let i in state.userInfo.roles) {
            // transRoles.push(role.role);
            if (state.userInfo.roles[i].role === "ROLE_USER") return true;
        }
        return false;
    },
    isTeacher: function () {
        let transRoles = [];
        for (let role of state.userInfo.roles) {
            transRoles.push(role.role);
        }
        if (transRoles.indexOf("ROLE_TEACHING_ADMIN") !== -1) return true;
        else return false;
    },
    getUserId: state => {
        return state.userInfo.id;
    }
};

// actions
const actions = {
    loginAction: ({ dispatch, commit }) => {
        axios.request({
            url: "/api/auth/signin",
            method: "post",
            data: {
                username: this.loginInfo.username,
                password: this.loginInfo.password
            }
        }).then((response) => {
            console.log(response.data);
            let getToken = response.data.accessToken;
            commit("loginSet", {
                username: this.loginInfo.username,
                accessToken: getToken
            });
            alert("登录成功");
            this.$http.request({
                url: "/api/users/info",
                method: 'get',
                headers: {
                    "Authorization": "Bearer " + getToken
                }
            }).then((infoResponse) => {
                this.$store.commit("infoSet", infoResponse.data);
                //console.log(state);
                if (infoResponse.data.roles[0].role === "ROLE_ADMIN") {
                    localStorage.setItem("managerToken", getToken);
                    // window.location = "/manager";
                    window.location.href = '/manage.html#/'
                }
                this.$router.push('/user');
            }).catch((error) => {
                console.log(error.response);
                if (error.response.data.status === 401) {
                    alert("获取用户信息出错");
                }
                else {
                    alert(error);
                }
            });
            //dispatch("loadUserInfo");
        }).catch((error) => {
            console.log(error.response);
            if (error.response.data.status === 401) {
                alert("用户名或密码错误");
            }
            else {
                alert(error);
            }
        });
    },
    loadUserInfo: ({ getters, commit }) => {
        axios.request({
            url: "/api/users/info",
            method: 'get',
            headers: getters.authRequestHead
        }).then((infoResponse) => {
            commit("infoSet", infoResponse.data);
            if (infoResponse.data.roles[0].role === "ROLE_ADMIN") {
                localStorage.setItem("managerToken", getToken);
                // window.location = "/manager";
                window.location.href = '/manage.html#/'
            }
            this.$router.push('/user');
        }).catch((error) => {
            console.log(error.response);
            if (error.response.data.status === 401) {
                alert("获取用户信息出错");
            }
            else {
                alert(error);
            }
        });
    }
};

// mutations
const mutations = {
    loginSet (state, loginData) {
        state.username = loginData.username;
        state.accessToken = loginData.accessToken;
        state.loginStatus = true;
        console.log(state);
    },
    infoSet (state, infoData) {
        state.userInfo = infoData;
        console.log(state);
    },
    logOut (state) {
        state.username = "";
        state.accessToken = "";
        state.loginStatus = false;
        state.userInfo = {
            email: "",
            grade: 0,
            major: "",
            realName: "",
            sex: "",
            sno: "",
            tno: "",
            tel: "",
            university: "",
            avatarUrl: "",
            roles: [
                {
                    id: 0,
                    role: "",
                }
            ]
        }
    },
    setNewAvatar (state, newUrl) {
        state.userInfo.avatarUrl = newUrl
    }
};

export default {
    state,
    getters,
    actions,
    mutations
}
