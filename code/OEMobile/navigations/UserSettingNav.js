import React from "react";
import { createStackNavigator } from 'react-navigation';
import UserSetting from "../screens/UserSetting.js";
import UserAvatarSetting from "../screens/UserAvatarSetting";
import UserEmailSetting from "../screens/UserEmailSetting";
import UserPasswordSetting from "../screens/UserPasswordSetting";
import UserInfoSetting from "../screens/UserInfoSetting";
import UserHeader from "../components/UserHeader"

export default createStackNavigator({
    UserSetting: {
        screen: UserSetting,
        navigationOptions: ({navigation}) => ({
            header: () => (
                <UserHeader navigation={navigation} title={"个人设置"}/>
            )
        })
    },
    UserAvatarSetting: UserAvatarSetting,
    UserEmailSetting: {
        screen: UserEmailSetting,
        navigationOptions: () => ({
            title: "修改邮箱"
        })
    },
    UserPasswordSetting: {
        screen: UserPasswordSetting,
        navigationOptions: () => ({
            title: "修改密码"
        })
    },
    UserInfoSetting: {
        screen: UserInfoSetting,
        navigationOptions: () => ({
            title: "修改个人信息"
        })
    }
}, {

})
