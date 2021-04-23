import React from "react";
import { createStackNavigator } from "react-navigation";
import CoursePaperList from "../screens/CoursePaperList";
import CoursePaper from "../screens/CoursePaper"
import CourseHeader from "./CourseChapterNav";

export default createStackNavigator({
    CoursePaperList: {
        screen: CoursePaperList,
        navigationOptions: ({ navigation }) => ({
            header: null
        })
    },
    CoursePaper
})
