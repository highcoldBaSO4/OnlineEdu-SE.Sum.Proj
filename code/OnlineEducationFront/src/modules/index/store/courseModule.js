import axios from 'axios'

axios.defaults.withCredentials = true;

const state = {
    id: 0,
    courseInfo: {},
    sections:{},
    identity: "",
    paperId:0,
    paperTitle:0,
    paperAnswers:0,
    studentSelectId:0,
    forumUpdate: false,
};

// getters
const getters = {
    getCourseId: state => {
        return state.id;
    },
    getCourseUrl: state => {
        return "/api/courses/" + state.id + "/";
    },
    getCourseInfo: state => {
        return state.courseInfo;
    },
    getCourseTitle: state => {
        return state.courseInfo.courseTitle;
    },
    getCourseImg: state => {
        if (state.courseInfo.avatarUrl !== "") {
            return "http://202.120.40.8:30382/online-edu/static/" + state.courseInfo.avatarUrl + "?a=" + Math.random();
        }
        else return "";
    },
    isCourseTeacher: state => {
        return state.identity !== "STUDENT";
    },
    getPaperById: (state) => (paperId) => {
        for (let paper of state.courseInfo.papers) {
            if (paper.id === paperId) {
                return paper;
            }
        }
    },
    getSectionList: (state) => {
        let sectionList = [];
        for (let sec of state.courseInfo.sectionList) {
            sectionList.push({
                secNo: sec.secNo,
                title: sec.title
            })
        }
        sectionList.sort((a, b) => {
            if (a.secNo < b.secNo) return -1;
            else return 1;
        });
        return sectionList;
    },
    getPaperId:state => {
        return state.paperId;
    },
    getPaperAnswers:state => {
        return state.paperAnswers;
    },
};

// actions
const actions = {
    // getThisCourseInfo(state){
    //     this.$http.request({
    //         url: '/api/courses/'+state.id+'/info',
    //         method: "get",
    //         headers: state.getters.authRequestHead,
    //     })
    //         .then(function (response) {
    //             console.log(response.data);
    //             state.commit("setCourseInfo",response.data);
    //             // alert("请求成功");
    //         })
    //         .catch(function (error) {
    //             console.log(error);
    //             // alert("请求失败");
    //         });
    // },
};

// mutations
const mutations = {
    setCourseId(state, courseId) {
        state.id = courseId;
    },
    setCourseInfo: (state, info) => {
        state.courseInfo = info;
        state.forumUpdate = false;
    },
    setIdentity: (state, identity) => {
        state.identity = identity;
    },
    setPaperId(state, paperId) {
        state.paperId=paperId;
    },
    setPaperTitle(state, paperTitle){
        state.paperTitle=paperTitle;
    },
    setPaperAnswers(state, paperAnswers) {
        state.paperAnswers=paperAnswers;
    },
    setStudentSelectId(state, studentSelectId) {
        state.studentSelectId=studentSelectId;
    },
    setForumUpdate(state, newState) {
        state.forumUpdate = newState;
    },
    setSetions(state, sections) {
        state.sections=sections;
    }
};

export default {
    state,
    getters,
    actions,
    mutations
}
