(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-74c92b3c"],{"0476":function(e,t,o){"use strict";var s=o("0e34"),n=o.n(s);n.a},"0e34":function(e,t,o){},"58da":function(e,t,o){"use strict";o.r(t);var s=function(){var e=this,t=e.$createElement,o=e._self._c||t;return o("div",[o("div",{staticClass:"login-square float-right"},[o("div",{staticClass:"login-form"},[o("h3",[e._v("登录")]),o("el-form",{ref:"loginInfo",attrs:{model:e.loginInfo,rules:e.rules}},[o("el-form-item",{attrs:{prop:"username"}},[o("el-input",{attrs:{placeholder:"用户名","suffix-icon":"el-icon-user",id:"userId"},model:{value:e.loginInfo.username,callback:function(t){e.$set(e.loginInfo,"username",t)},expression:"loginInfo.username"}})],1),o("el-form-item",{attrs:{prop:"password"}},[o("el-input",{attrs:{type:"password",placeholder:"密码","suffix-icon":"el-icon-lock"},model:{value:e.loginInfo.password,callback:function(t){e.$set(e.loginInfo,"password",t)},expression:"loginInfo.password"}})],1),o("el-form-item",[o("el-button",{attrs:{type:"primary",size:"medium"}},[o("div",{staticClass:"login-text",attrs:{loading:!0},on:{click:e.login}},[e._v("登录")])])],1),o("el-form-item",[o("div",{staticClass:"float-left"},[o("el-link",{attrs:{underline:!1}},[e._v("忘记密码")])],1),o("div",{staticClass:"float-right"},[o("el-link",{attrs:{underline:!1,href:"/#/register"}},[e._v("注册新用户")])],1),o("div",{staticClass:"float-clear"})])],1)],1)]),o("div",{staticStyle:{clear:"both"}})])},n=[],a={name:"Login",data:function(){return{loginInfo:{username:"",password:""},rules:{username:[{min:3,max:50,message:"用户名长度为3到50个字符",trigger:"blur"}],password:[{min:6,max:15,message:"密码长度为6到15个字符",trigger:"blur"}]}}},methods:{login:function(){var e=this;this.$refs["loginInfo"].validate(function(t){t&&e.$http.request({url:"/api/auth/signin",method:"post",data:{username:e.loginInfo.username,password:e.loginInfo.password}}).then(function(t){console.log(t.data);var o=t.data.accessToken;e.$store.commit("loginSet",{username:e.loginInfo.username,accessToken:o}),alert("登录成功"),e.$http.request({url:"/api/users/info",method:"get",headers:{Authorization:"Bearer "+o}}).then(function(t){e.$store.commit("infoSet",t.data),"ROLE_ADMIN"===t.data.roles[0].role&&(localStorage.setItem("managerToken",o),window.location="/manager"),e.$router.push("/user")}).catch(function(e){console.log(e.response),401===e.response.data.status?alert("获取用户信息出错"):alert(e)})}).catch(function(e){console.log(e.response),401===e.response.data.status?alert("用户名或密码错误"):alert(e),reject()})})}}},r=a,i=(o("0476"),o("2877")),l=Object(i["a"])(r,s,n,!1,null,"083a3ed4",null);t["default"]=l.exports}}]);
//# sourceMappingURL=chunk-74c92b3c.1a4e6fdd.js.map