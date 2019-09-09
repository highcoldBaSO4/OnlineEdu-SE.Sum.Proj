import axios from 'axios';
import { Message } from "element-ui";

let errorMsg = (errorText) => Message({
    type: "error",
    text: errorText,
    showCancel: true
});

export default {
    passwordRule: [
        {
            required: true,
            message: "密码不能为空",
            trigger: "change"
        },
        {
            min: 6,
            max: 15,
            message: "密码长度为6到15个字符",
            trigger: "blur"
        }
    ],
    telRule: [
        {
            required: true,
            message: "手机号不能为空",
            trigger: "change"
        },
        {
            len: 11,
            message: "手机号必须为11位",
            trigger: "blur"
        },
        {
            validator: (rule, value, callback) => {
                let temp = ['3','4','5','7','8'];
                if (value[0] === '1' && temp.includes(value[1])) {
                    callback();
                }
                else {
                    callback(new Error("手机号不可用，请输入有效的手机号"));
                }
            },
            trigger: "blur"
        },
        {
            validator: (rule, value, callback) => {
                axios.request({
                    url: '/api/users/checkSame/tel',
                    request: 'get',
                    params: {
                        tel: value
                    }
                }).then((response) => {
                    if (response.data) {
                        callback(new Error("手机号已被使用"));
                    }
                    else {
                        callback();
                    }
                }).catch((error) => {
                    console.log(error.response);
                    errorMsg("无法检查手机号重复，请检查网络或联系管理员")
                })
            },
            trigger: "blur"
        }
    ],
    emailRule: [
        {
            required: true,
            message: "邮箱不能为空",
            trigger: "change"
        },
        {
            type: "email",
            message: "邮箱格式有误",
            trigger: "blur"
        },
        {
            validator: (rule, value, callback) => {
                axios.request({
                    url: '/api/users/checkSame/email',
                    request: 'get',
                    params: {
                        email: value
                    }
                }).then((response) => {
                    if (response.data) {
                        callback(new Error("邮箱已被使用"));
                    }
                    else {
                        callback();
                    }
                }).catch((error) => {
                    console.log(error.response);
                    errorMsg("无法检查邮箱重复，请检查网络或联系管理员")
                })
            },
            trigger: "blur"
        }
    ],
    requireRule: function (message) {
        return {
            required: true,
            message: message,
            trigger: "change"
        }
    },
    usernameRule: [
        {
            required: true,
            message: "用户名不能为空",
            trigger: "change"
        },
        {
            min: 3,
            max: 50,
            message: "用户名长度为3到50个字符",
            trigger: "change"
        },
        {
            validator: (rule, value, callback) => {
                axios.request({
                    url: '/api/users/checkSame/username',
                    request: 'get',
                    params: {
                        username: value
                    }
                }).then((response) => {
                    if (response.data) {
                        callback(new Error("用户名已被使用"));
                    }
                    else {
                        callback();
                    }
                }).catch((error) => {
                    console.log(error.response);
                    errorMsg("无法检查用户名重复，请检查网络或联系管理员")
                })
            },
            trigger: "blur"
        }
    ]
}
