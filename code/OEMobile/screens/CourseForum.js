import React, {Component} from 'react';
import { FlatList } from 'react-native';
import { connect } from 'react-redux';
import { Container, View, ListItem, Text, Card, CardItem, Left, Right, Button, Icon } from "native-base";
import UserUnit from '../components/UserUnit';
import {setCurrentTopic} from "../store/forumActions";
import {NavigationEvents} from "react-navigation"

class CourseForum extends Component {
    constructor(props) {
        super(props);
        this.state = {
            sectionForum: []
        }
    }

    initForum = () => {
        global.showLoading("加载论坛中");
        this.$axios.request({
            url: `/api/courses/${this.props.courseId}/forums`,
            method: "get",
            headers: this.props.authHeader
        }).then((response) => {
            global.showLoading("论坛初始化中");
            let sectionForum = [];
            for (let sec of this.props.sectionList) {
                sectionForum.push({
                    secNo: sec.secNo,
                    title: sec.title
                })
            }
            for (let i in sectionForum) {
                sectionForum[i].topics = []
            }
            let forum = response.data;
            forum.sort((a,b) => {
                if (a.secNo === b.secNo) {
                    if (a.path < b.path) return -1;
                    else return 1;
                }
                else {
                    if (a.secNo < b.secNo) return -1;
                    else return 1;
                }
            });
            console.log(forum);
            let scanSecNum = 0;
            let secLength = sectionForum.length;
            let forumStack = [{}];
            let pathLevel = 1;
            for (let i of forum) {
                i.responses = [];
                let pathArr = i.path.split("/");
                let pathLength = pathArr.length;
                if (pathLength === 2) {
                    pathLevel = 2;
                    forumStack.shift();
                    forumStack.unshift(i);
                    for (; scanSecNum < secLength; ++scanSecNum) {
                        if (sectionForum[scanSecNum].secNo === i.secNo) {
                            sectionForum[scanSecNum].topics.push(i);
                            break;
                        }
                    }
                }
                else {
                    if (pathLength <= pathLevel) {
                        let popNum = pathLevel - pathLength + 1;
                        for (let j = 0; j < popNum; ++j) {
                            forumStack.shift();
                        }
                        pathLevel = popNum;
                    }
                    i.responseTo = forumStack[0].userId;
                    forumStack[0].responses.push(i);
                    forumStack.unshift(i);
                    pathLevel = pathLength;
                }
            }
            console.log(sectionForum);
            this.setState({
                sectionForum
            });
            global.cancelLoading();
        }).catch((error) => {
            console.log(error.response);
            alert(error);
        })
    };

    enterResponses = (topic) => {
        if (topic.title === "该内容已被锁" && topic.content === "该内容已被锁") {
            this.$toast.errorToast("论坛被锁，无法查看");
        }
        else {
            this.props.setCurrentTopic(topic);
            this.props.navigation.navigate("CourseForumResponses", {
                title: topic.title
            });
        }
    };

    renderTopicUnit = (item) => (
        <Card>
            <CardItem header button onPress={() => {this.enterResponses(item)}}>
                <Left>
                    <UserUnit user={item.userId}/>
                </Left>
                <Right>
                    <Text note>{item.createdAt}</Text>
                </Right>
            </CardItem>
            <CardItem button onPress={() => {this.enterResponses(item)}}>
                <Text>{item.title}</Text>
            </CardItem>
        </Card>
    );

    componentDidMount(): void {
        this.initForum();
    }

    render() {
        return (
            <Container>
                <NavigationEvents onWillFocus={() => {this.initForum()}}/>
                <FlatList
                    data={this.state.sectionForum}
                    renderItem={({ item }) => {
                        return (
                            <View>
                                <ListItem itemDivider>
                                    <Left>
                                        <Text>{item.title}</Text>
                                    </Left>
                                    <Right>
                                        <Button transparent onPress={() => {this.props.navigation.navigate("CourseAddTopic", {
                                            secNo: item.secNo
                                        })}}>
                                            <Icon name={"add"} type={"Ionicons"} />
                                            {/*<Text>发帖</Text>*/}
                                        </Button>
                                    </Right>
                                </ListItem>
                                {/*<FlatList data={item.topics} renderItem={({ item }) => this.renderTopicUnit(item)}/>*/}
                                {
                                    item.topics.map((item, index) => this.renderTopicUnit(item))
                                }
                            </View>
                        )
                    }}
                />
            </Container>
        );
    }
}

function mapStateToProps(state) {
    return {
        courseId: state.courseInfo.id,
        authHeader: state.login.authHeader,
        sectionList: state.courseInfo.sectionList
    }
}

const mapDispatchToProps = dispatch => {
    return {
        setCurrentTopic: (topic) => dispatch(setCurrentTopic(topic))
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(CourseForum);
