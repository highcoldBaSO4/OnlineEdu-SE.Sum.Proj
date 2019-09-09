export const SET_LOGIN = 'SET_LOGIN';
export const SET_LOGOUT = 'SET_LOGOUT';
export const SET_USERINFO = 'SET_USERINFO';
export const SET_NEW_AVATAR = "SET_NEW_AVATAR";

export function setLogin(username, accessToken) {
    return {
        type: SET_LOGIN,
        login: {
            username: username,
            accessToken: accessToken,
            loginStatus: true
        }
    }
}

export function setLogout() {
    return {
        type: SET_LOGOUT
    }
}

export function setNewAvatar(newAvatar) {
    return {
        type: SET_NEW_AVATAR,
        newAvatar: newAvatar
    }
}
