import React from "react";
import { createStackNavigator } from "react-navigation";
import CourseForum from "../screens/CourseForum";
import CourseAddTopic from "../screens/CourseAddTopic";
import CourseForumResponses from "../screens/CourseForumResponses";
import CourseAddResponse from "../screens/CourseAddResponse"

export default createStackNavigator({
    CourseForum: {
        screen: CourseForum,
        navigationOptions: {
            header: null
        }
    },
    CourseAddTopic: {
        screen: CourseAddTopic

    },
    CourseForumResponses: {
        screen: CourseForumResponses,
        navigationOptions: ({navigation}) => ({
            title: navigation.getParam("title")
        })
    },
    CourseAddResponse: {
        screen: CourseAddResponse
    }
})
