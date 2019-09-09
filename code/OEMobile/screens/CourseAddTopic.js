import React, {Component} from 'react';
import { connect } from "react-redux";
import { FlatList, Alert, Image, Dimensions } from 'react-native';
import { Container, Content, Form, Item, Text, Input, Textarea, Label, Button, View, CardItem, Left, Right } from "native-base";
import ImagePicker from 'react-native-image-crop-picker';

class CourseAddTopic extends Component {
    constructor(props) {
        super(props);
        this.state = {
            title: "",
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
            images: temp,
            test: true
        });
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
                            console.log(images[0].path);
                            //console.log(this.state.images);
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
        let secNo = this.props.navigation.getParam("secNo");
        this.$axios.request({
            url: `/api/courses/${courseId}/sections/${secNo}/forums`,
            method: "post",
            data: {
                title: this.state.title,
                content: this.state.content
            },
            headers: this.props.authHeader
        }).then((response) => {
            this.$toast.successToast("帖子发布成功");
            console.log(response);
            this.props.navigation.popToTop();
            // //global.showLoading("发布帖子中");
            // //console.log(this.state.images);
            // console.log(res);
            // let formdata = new FormData();
            // let tempList = [];
            // for (let i of this.state.images) {
            //     console.log(i.uri);
            //     formdata.append("images", {
            //         uri: i.path.replace("file:///", "content://"),
            //         type: i.mime,
            //         name: i.path.split("/").pop()
            //     });
            //     // tempList.push({
            //     //     uri: i.uri,
            //     //     type: i.type,
            //     //     name: i.fileName
            //     // })
            // }
            // //formdata.append("images", tempList);
            // // this.$axios.request({
            // //     url: `/api/forums/${res.data.id}/images`,
            // //     method: "post",
            // //     data: formdata,
            // //     headers: {
            // //         ...this.props.authHeader,
            // //         'Content-Type': 'multipart/form-data'
            // //     }
            // // }).then((response) => {
            // //     this.$toast.successToast("帖子发布成功");
            // //     console.log(response);
            // // }).catch((error) => {
            // //     this.$toast.errorToast("图片发布失败");
            // //     console.log(error);
            // //     console.log(error.response);
            // // })
        }).catch((error) => {
            this.$toast.errorToast("帖子发布失败");
            console.log(error);
        })
    };

    render() {
        let imgWidth = this.$window.width * 0.9;

        return (
            <Container>
                <Content>
                    <Form>
                        <Item stackedLabel>
                            <Label>标题</Label>
                            <Input
                                defaultValue={this.state.title}
                                onChangeText={(text) => {this.setState({title: text})}}
                            />
                        </Item>
                        <Textarea
                            rowSpan={6}
                            placeholder={"在此添加内容"}
                            defaultValue={this.state.content}
                            onChangeText={(text) => {this.setState({content: text})}}
                        />
                    </Form>
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
                            <Button transparent onPress={() => {this.chooseImages()}} disabled>
                                <Text>暂不支持添加图片</Text>
                            </Button>
                        </Left>
                        <Right>
                            <Button transparent onPress={() => {this.commitAddTopic()}}>
                                <Text>发布帖子</Text>
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
        authHeader: state.login.authHeader
    }
}

export default connect(mapStateToProps, null)(CourseAddTopic);
