import React from "react";
import { createBottomTabNavigator } from 'react-navigation';
import UserCourseList from "../screens/UserCourseList";
import UserSettingNav from "./UserSettingNav";
import { Icon, Header } from "native-base";

export default createBottomTabNavigator({
    UserCourseList: {
        screen: UserCourseList,
        navigationOptions: () => ({
            header: null,
            title: "我的课程",
            tabBarIcon: () => (
                <Icon name={"list"} type={"FontAwesome"} />
            )
        })
    },
    UserSettingNav: {
        screen: UserSettingNav,
        navigationOptions: () => ({
            header: null,
            title: "个人设置",
            tabBarIcon: () => (
                <Icon name={"settings"} type={"Ionicons"} />
            )
        })
    }
}, {
    tabBarOptions: {
        activeBackgroundColor: "#eeeeee",
    }
})
