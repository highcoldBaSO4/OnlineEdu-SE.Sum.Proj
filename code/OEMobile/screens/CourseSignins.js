import React, {Component} from 'react';
import { FlatList, PermissionsAndroid, ScrollView } from "react-native";
import {Container, List, ListItem, Text, Button, Icon, View} from "native-base";
import CourseHeader from "../components/CourseHeader";
import UserFab from "../components/UserFab";
import {connect} from "react-redux";
import Geolocation from "react-native-geolocation-service"

class CourseSignins extends Component {
    constructor(props) {
        super(props);
        this.showDrawer = this.showDrawer.bind(this);
        this.state = {
            signIns: []
        };
        this.initSignins();
    }

    initSignins = () => {
        global.showLoading("获取签到中");
        this.signins = [];
        this.$axios.request({
            url: this.signInUrl(),
            method: "get",
            headers: {
                Authorization: "Bearer " + this.props.accessToken
            }
        }).then((response) => {
            this.signins = response.data;
            console.log(this.signins);
            this.setState({
                signIns: response.data
            })
        }).catch((error) => {
            this.$toast.errorToast("获取签到出错");
            console.log(error.response);
        })
    };

    async requestLocationPermission() {
        try {
            const granted = await PermissionsAndroid.request(
                PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION,
                {
                    title: '申请访问位置权限',
                    message: '签到功能需要位置权限才可以使用',
                    //buttonNeutral: 'Ask Me Later',
                    buttonNegative: '取消',
                    buttonPositive: '访问',
                },
            );
            if (granted === PermissionsAndroid.RESULTS.GRANTED) {
                console.log('Location permission granted');
            } else {
                console.log('Camera permission denied');
            }
        } catch (err) {
            console.warn(err);
        }
    }

    componentWillMount(): void {
        this.requestLocationPermission();
    }

    startSignIn = (signInNo) => {
        Geolocation.getCurrentPosition(
            (position => {
                global.showLoading("签到中");
                this.$axios.request({
                    url: "/api/users/" + this.props.userId + "/signIns",
                    method: "post",
                    headers: {
                        Authorization: "Bearer " + this.props.accessToken
                    },
                    data: {
                        courseId: this.props.courseId,
                        signInNo: signInNo,
                        latitude: position.coords.latitude,
                        longitude: position.coords.longitude
                    }
                }).then(() => {
                    this.$toast.successToast("签到成功！");
                    this.initSignins();
                }).catch((error) => {
                    this.$toast.errorToast(error.response.data);
                    console.log(error.response)
                });
                console.log(signInNo);
                console.log(position);
            }),
            (error => {
                console.log(error.code, error.message);
                let errorText = "";
                switch (error.code) {
                    case 1:
                        errorText = "未获得位置权限";
                        break;
                    case 2:
                        errorText = "无法获取当前位置";
                        break;
                    case 3:
                        errorText = "位置获取超时";
                        break;
                    case 4:
                        errorText = "Google Play 服务出错";
                        break;
                    case 5:
                        errorText = "未开启定位";
                        break;
                    default:
                        errorText = "系统出错";
                        break;
                }
                this.$toast.errorToast(errorText);
            }),
            { enableHighAccuracy: true, timeout: 15000, maximumAge: 10000 }
        )

    };

    signInUrl = () => {
        return "/api/courses/" + this.props.courseId + "/signIns"
    };

    statusText = (status) => {
        if (status) return "已签到";
        else return "未签到"
    };

    showDrawer() {
        this.props.navigation.openDrawer()
    }

    _signInLine(signIn) {
        return (
            <ListItem>
                <View style={{flexDirection: "row"}}>
                    <View style={{flex: 5}}>
                        <Text>签到：{signIn.signIn.startDate + " ~ " + signIn.signIn.endDate + "  " + this.statusText(signIn.ok)}</Text>
                    </View>
                    <View style={{flex: 1}}>
                        <Button icon onPress={() => {this.startSignIn(signIn.signIn.signInPrimaryKey.signInNo)}}>
                            <Icon name={"sign-in"} type={"FontAwesome"}/>
                        </Button>
                    </View>
                </View>
            </ListItem>
        )
    }

    render() {
        return (
            <ScrollView>
                <List>
                    <FlatList data={this.state.signIns} renderItem={({item}) => this._signInLine(item)}/>
                </List>
            </ScrollView>
        );
    }
}

function mapStateToProps(state) {
    return {
        courseId: state.courseInfo.id,
        accessToken: state.login.accessToken,
        userId: state.userInfo.id
    }
}

export default connect(mapStateToProps, null)(CourseSignins);
