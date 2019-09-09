import React, { Component } from "react";
import CourseInfo from "../components/CourseInfo";
import { Container, Content } from "native-base";
import CourseHeader from "../components/CourseHeader";
import UserFab from "../components/UserFab";

class CourseHome extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <Container>
                <Content>
                    <CourseInfo/>
                </Content>
            </Container>
        )
    }
}

export default CourseHome;
