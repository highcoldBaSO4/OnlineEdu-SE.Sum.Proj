import React, { Component } from "react";
import { Container, Content, Accordion } from "native-base";

const announcement = [
    {
        title: "First Notice",
        content: "hahahaha\nijdijfidjfijdi"
    }
]

class CourseAnnouncement extends Component {
    render() {
        return (
            <Container>
                <Content>
                    <Accordion dataArray={announcement}/>
                </Content>
            </Container>
        )
    }
}

export default CourseAnnouncement;
