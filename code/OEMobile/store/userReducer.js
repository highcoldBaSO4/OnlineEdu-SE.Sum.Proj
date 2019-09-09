import {
    SET_LOGIN,
    SET_LOGOUT,
    SET_USERINFO,
    SET_NEW_AVATAR
} from './actions';

const loginInit = {
    username: "",
    accessToken: "",
    loginStatus: false,
    authHeader: {}
};

const userInfoInit = {
    assistCourses: [],
    learnCourses: [],
    teachCourses: [],
    email: "",
    grade: 0,
    major: "",
    realName: "",
    sex: "",
    sno: "",
    tno: "",
    id: "",
    tel: "",
    university: "",
    avatarUrl: "",
    roles: [
        {
            id: 0,
            role: "",
        }
    ]
};

function loginAction(state = loginInit, action) {
    switch (action.type) {
        case SET_LOGIN:
            let newLogin = action.login;
            newLogin.authHeader = {
                Authorization: "Bearer " + newLogin.accessToken
            };
            return newLogin;
        case SET_LOGOUT:
            return state;
        default:
            return state;
    }
}

function userInfoAction(state = userInfoInit, action) {
    switch (action.type) {
        case SET_USERINFO:
            return action.userInfo;
        case SET_NEW_AVATAR:
            return Object.assign({}, state, {
                avatarUrl: action.newAvatar
            });
    }
    return state;
}

export default {
    login: loginAction,
    userInfo: userInfoAction
}
