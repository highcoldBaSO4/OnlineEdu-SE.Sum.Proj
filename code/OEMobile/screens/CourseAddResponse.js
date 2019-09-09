import React, {Component} from 'react';
import { connect } from "react-redux";
import { FlatList, Alert, Image, Dimensions } from 'react-native';
import { Container, Content, Form, Item, Text, Input, Textarea, CardItem, Left, Right, Button, View } from "native-base";
import ImagePicker from 'react-native-image-crop-picker';

class CourseAddResponse extends Component {
    constructor(props) {
        super(props);
        this.state = {
            content: "",
            images: [],
            test: false
        }
        //this.images = [];
    }

    addImage = (image) => {
        let temp = this.state.images;
        temp.push(image);
        this.setState({
            content: "",
            images: temp,
            test: true
        });
        console.log(this.state.images.length);
    };

    chooseImages = () => {
        Alert.alert(
            "添加图片",
            "请选择添加图片方式",
            [
                {
                    text: "打开相机",
                    onPress: () => {
                        ImagePicker.openCamera({
                            includeBase64: true
                        }).then((image) => {
                            console.log(typeof image);
                            this.addImage(image);
                        })
                    }
                },
                {
                    text: "从相册中选取",
                    onPress: () => {
                        ImagePicker.openPicker({
                            multiple: true,
                            includeBase64: true
                        }).then((images) => {
                            console.log(images[0].mime);
                            console.log(this.state.images);
                            for (let i of images) {
                                this.addImage(i);
                            }
                        })
                    }
                }
            ])
    };

    commitAddTopic = () => {
        global.showLoading("发布帖子中");
        let courseId = this.props.courseId;
        let secNo = this.props.secNo;
        let forumPath = this.props.navigation.getParam("path");
        this.$axios.request({
            url: `/api/courses/${courseId}/sections/${secNo}/forums`,
            method: "post",
            data: {
                content: this.state.content,
                path: forumPath
            },
            headers: this.props.authHeader
        }).then((response) => {
            this.$toast.successToast("回复发布成功");
            console.log(response);
            this.props.navigation.popToTop();
        }).catch((error) => {
            this.$toast.errorToast("回复发布失败");
            console.log(error);
        })
    };

    render() {
        let imgWidth = this.$window.width * 0.9;

        return (
            <Container>
                <Content>
                    <Textarea
                        rowSpan={6}
                        placeholder={"在此添加内容"}
                        defaultValue={this.state.content}
                        onChangeText={(text) => {this.setState({content: text})}}
                    />
                    <View>
                        {
                            this.state.images.map((item, index) => {
                                return (
                                    <Image
                                        key={index}
                                        source={{uri: `data:${item.mime};base64,${item.data}`}}
                                        style={{
                                            width: imgWidth,
                                            height: imgWidth / item.width * item.height,
                                            marginLeft: "auto",
                                            marginRight: "auto"
                                        }}
                                    />
                                )
                            })
                        }
                    </View>
                    <CardItem>
                        <Left>
                            <Button onPress={() => {this.chooseImages()}} transparent disabled>
                                <Text>暂不支持添加图片</Text>
                            </Button>
                        </Left>
                        <Right>
                            <Button onPress={() => {this.commitAddTopic()}} transparent>
                                <Text>添加回复</Text>
                            </Button>
                        </Right>
                    </CardItem>
                </Content>
            </Container>
        );
    }
}

function mapStateToProps(state) {
    return {
        courseId: state.courseInfo.id,
        authHeader: state.login.authHeader,
        secNo: state.topic.secNo
    }
}

export default connect(mapStateToProps, null)(CourseAddResponse);
