import React, {Component} from 'react';
import { Container, Content, Text, ListItem } from "native-base";
import { connect } from "react-redux";
import CourseForumTopic from "../components/CourseForumTopic";
import CourseForumResponseUnit from "../components/CourseForumResponseUnit";

class CourseForumResponses extends Component {
    constructor(props) {
        super(props);
    }

    addResponse = (path) => {
        this.props.navigation.navigate("CourseAddResponse", {
            path
        })
    };

    render() {
        return (
            <Container>
                <Content>
                    <CourseForumTopic forumTopic={this.props.topic} navigation={this.props.navigation}/>
                    <ListItem itemDivider>
                        <Text>回复</Text>
                    </ListItem>
                    {
                        this.props.responses.map((item,index) => {
                            return <CourseForumResponseUnit response={item} addResponse={this.addResponse} />
                        })
                    }
                </Content>
            </Container>
        );
    }
}

function mapStateToProps(state) {
    return {
        topic: state.topic,
        responses: state.topic.responses
    }
}

export default connect(mapStateToProps, null)(CourseForumResponses);
