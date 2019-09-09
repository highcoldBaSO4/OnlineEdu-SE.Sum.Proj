import React from "react";
import {FlatList, Image, View, StyleSheet, TouchableOpacity, Dimensions} from 'react-native';
import {Container, Header, Left, Body, Text, Title, Right, Button, Icon, Thumbnail} from 'native-base';
import UserUnit from "../components/UserUnit";
import UserHeader from "../components/UserHeader";
import { connect } from 'react-redux';

class UserCourseList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            activeFab: 'true',
            showCourses: '教授课程',
            courses: []
        };
        this.initialCourseData();
    }

    initialCourseData() {
        global.showLoading("加载课程列表");
        this.$axios.request({
            url: "/api/users/info/courses/learn",
            method: "get",
            headers: {
                "Authorization": "Bearer " + this.props.accessToken
            }
        }).then((response) => {
            //global.cancelLoading();
            console.log(response.data);
            this.setState({courses: response.data});
        }).catch((error) => {
            this.$toast.errorToast("获取课程列表出错");
            alert(error);
        })
    }

    enterCourse(courseId) {
        global.showLoading("加载课程");
        this.$axios.request({
            url: "/api/courses/" + courseId + "/info",
            method: "get",
            headers: {
                "Authorization": "Bearer " + this.props.accessToken
            }
        }).then((response) => {
            this.props.setCourseInfo(response.data.course);
            this.props.navigation.navigate("Course", {courseId: courseId});
            //global.cancelLoading();
        }).catch((error) => {
            //global.cancelLoading();
            this.$toast.errorToast("获取课程信息出错");
            console.log(error.response);
        })
    }

    _createCourseCard(course) {
        //console.log(course.teacher);
        return (
            <TouchableOpacity onPress={() => {this.enterCourse(course.id)}}>
                <View style={styles.mainCard}>
                    <View style={styles.imgView}>
                        <Image style={styles.courseImg} source={{uri: "http://202.120.40.8:30382/online-edu/static/" + course.avatarUrl}}/>
                    </View>
                    <View style={styles.textView}>
                        <View style={styles.titleText}>
                            <Text style={styles.titleFont}>{course.courseTitle}</Text>
                        </View>
                        <View style={styles.secondLine}>
                            <View style={{flex: 1}}>
                                <UserUnit user={course.teacher}/>
                            </View>
                            <View style={{flex: 1, flexDirection: "row"}}>
                                <View>
                                    <Icon name={"compass"} type={"Ionicons"} style={{fontSize: 22, marginTop: 5}} />
                                </View>
                                <View>
                                    <Text style={styles.locationText}>
                                        { " " + course.location }
                                    </Text>
                                </View>
                            </View>
                        </View>
                        {/*<Text style={styles.thirdLine}>*/}
                            {/*<Icon name={"calendar"} />*/}
                            {/*2019-07-12 ~ 2019-08-29*/}
                        {/*</Text>*/}
                    </View>
                </View>
            </TouchableOpacity>
        )
    }

    render() {
        return (
            <Container>
                <UserHeader navigation={this.props.navigation} title={"我的课程"}/>
                <FlatList data={this.state.courses} renderItem={({item}) => this._createCourseCard(item)}/>
            </Container>
        );
    }
}

function mapStateToProps(state) {
    return {
        accessToken: state.login.accessToken,
        userInfo: state.userInfo,
        authHeader: state.authHeader
    }
}

const mapDispatchToProps = dispatch => {
    return {
        setCourseInfo: (courseInfo) => dispatch({
            type: "SET_COURSE_INFO",
            courseInfo
        })
    }
};

const styles = StyleSheet.create({
    mainBack: {
        backgroundColor: "#ddd"
    },
    mainCard: {
        height: 100,
        borderStyle: "solid",
        borderWidth: 1,
        borderColor: "#666",
        flexDirection: "row",
        marginTop: 10,
        backgroundColor: "#fafafa"
    },
    imgView: {
        //flex: 3,
        //backgroundColor: "yellow"
    },
    courseImg: {
        height: 98,
        width: 162,
        //backgroundColor: "blue"
    },
    textView: {
        //flex: 4,
        width: Dimensions.get("window").width - 162,
        flexDirection: "column"
    },
    titleText: {
        flex: 110,
        fontWeight: "bold",
        fontSize: 20,
        paddingTop: 5,
        flexShrink: 1,
        paddingLeft: 2
    },
    titleFont: {
        fontWeight: "bold",
        fontSize: 17,
    },
    secondLine: {
        flex: 126,
        flexDirection: "row",
    },
    thirdLine: {
        flex: 50,
    },
    locationText: {
        marginTop: 10,
        fontSize: 13
    }
});

export default connect(mapStateToProps, mapDispatchToProps)(UserCourseList);
