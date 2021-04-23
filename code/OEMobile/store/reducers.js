// import {
//     SET_LOGIN,
//     SET_LOGOUT,
//     SET_USERINFO
// } from './actions';
// import {
//     SET_COURSE_ID,
//     SET_COURSE_INFO
// } from "./courseActions";
// import {combineReducers} from 'redux';
//
// const user = {
//     login: {
//         username: "",
//         accessToken: "",
//         loginStatus: false,
//         authHeader: {
//             Authorization: ""
//         }
//     },
//     userInfo: {
//         assistCourses: [],
//         learnCourses: [],
//         teachCourses: [],
//         email: "",
//         grade: 0,
//         major: "",
//         realName: "",
//         sex: "",
//         sno: "",
//         tno: "",
//         id: "",
//         tel: "",
//         university: "",
//         avatarUrl: "",
//         roles: [
//             {
//                 id: 0,
//                 role: "",
//             }
//         ]
//     }
// }
//
// //const { loginInit, userInfoInit } = user;
// const loginInit = {
//     username: "",
//     accessToken: "",
//     loginStatus: false,
// }
//
// const userInfoInit = {
//     assistCourses: [],
//     learnCourses: [],
//     teachCourses: [],
//     email: "",
//     grade: 0,
//     major: "",
//     realName: "",
//     sex: "",
//     sno: "",
//     tno: "",
//     id: "",
//     tel: "",
//     university: "",
//     avatarUrl: "",
//     roles: [
//         {
//             id: 0,
//             role: "",
//         }
//     ]
// };
//
// const courseInfoInit = {
//     avatarUrl: "",
//     courseTitle: "",
//     endDate:"2019-08-09T16:00:00.000+0000",
//     id: 8,
//     learns: [],
//     location: "",
//     notices: [],
//     papers: [],
//     sectionList: [],
//     signIns: [],
//     startDate: [],
//     state: "",
//     students: [],
//     teacher: {},
//     teacherAssistants: [],
//     timeSlots: [],
// };
//
// function loginAction(state = loginInit, action) {
//     switch (action.type) {
//         case SET_LOGIN:
//             let newLogin = action.login;
//             newLogin.authHeader = {
//                 Authorization: "Bearer " + newLogin.accessToken
//             };
//             return newLogin;
//         case SET_LOGOUT:
//             return state;
//         default:
//             return state;
//     }
// }
//
// function userInfoAction(state = userInfoInit, action) {
//     switch (action.type) {
//         case SET_USERINFO:
//             return action.userInfo;
//     }
//     return state;
// }
//
// function courseInfoAction(state = courseInfoInit, action) {
//     switch (action.type) {
//         case SET_COURSE_INFO:
//             return action.courseInfo;
//         default:
//             return state;
//     }
// }
import {combineReducers} from 'redux';
import courseReducer from "./courseReducer.js";
import userReducer from "./userReducer.js";
import sectionReducer from "./sectionReducer";
import paperReducer from "./paperReducer";
import forumReducer from "./forumReducer";

const OEMobileApp = combineReducers({
    ...userReducer,
    ...courseReducer,
    ...sectionReducer,
    ...paperReducer,
    ...forumReducer
})

export default OEMobileApp;
