import React from "react";
import { createStackNavigator, createAppContainer } from "react-navigation";
import LoginScreen from "../screens/LoginScreen";
import UserNav from "./UserNav";
import CourseNav from "./CourseNav"
import CourseHeader from "../components/CourseHeader";

const TopNav = createStackNavigator({
    Login: {
        screen: LoginScreen,
        navigationOptions: () => ({
            header: null
        })
    },
    Home: {
        screen: UserNav,
        navigationOptions: () => ({
            header: null
        })
    },
    Course: {
        screen: CourseNav,
        navigationOptions: ({ navigation }) => ({
            header: () => (
                <CourseHeader navigation={navigation} />
            )
        })
    }
});

export default createAppContainer(TopNav);
