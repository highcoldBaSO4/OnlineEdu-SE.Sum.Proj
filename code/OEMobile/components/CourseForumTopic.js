import React, {Component} from 'react';
import {Image, TouchableOpacity} from 'react-native';
import { connect } from 'react-redux';
import { CardItem, Left, Body, Right, Text, Button, View } from "native-base";
import UserUnit from '../components/UserUnit';
import ImageViewer from "./ImageViewer";
import { setCurrentTopic } from "../store/forumActions";

class CourseForumTopic extends Component {
    constructor(props) {
        super(props);
        let imgLength = this.props.forumTopic.imageUrls.length;
        let initSize = new Array(imgLength);
        initSize.fill(1);
        let imgList = [];
        for (let i of this.props.forumTopic.imageUrls) {
            imgList.push({
                url: `http://202.120.40.8:30382/online-edu/static/${i}`
            })
        }
        console.log(imgList);
        this.state = {
            imgSize: initSize,
            showViewer: false,
            currentImg: 0,
            imgList
        }
    }

    // saveImage = (url) => {
    //     RNFetchBlob.config({
    //         path: `${RNFetchBlob.fs.dirs.PictureDir}/oeMobile`
    //     }).fetch('GET', url, {
    //
    //     }).progress((received, total) => {
    //         global.showLoading(`图片保存中：${Math.trunc(received/total*100)}%`);
    //     }).then(() => {
    //         global.cancelLoading();
    //         this.$toast.successToast("图片保存成功！");
    //     }).catch((error) => {
    //         global.cancelLoading();
    //         this.$toast.errorToast("出错啦！" + error);
    //     })
    // };

    // enterResponses = () => {
    //     this.props.setCurrentTopic(this.props.forumTopic);
    //     this.props.navigation.navigate("CourseForumResponses");
    // };

    render() {
        let topic = this.props.forumTopic;
        let imgWidth = this.$window.width * 0.9;
        let imgSize = this.state.imgSize;

        return (
            <View>
                <View>
                    <CardItem header>
                        {/*<Body>*/}
                        {/*<Text>{topic.title}</Text>*/}
                        {/*</Body>*/}
                        <Left>
                            <UserUnit user={topic.userId}/>
                        </Left>
                        <Right>
                            <Text note>{topic.createdAt}</Text>
                        </Right>
                    </CardItem>
                    <CardItem>
                        <Body>
                        <Text>{topic.content}</Text>
                        {
                            topic.imageUrls.map((item, index) => {
                                return (<TouchableOpacity onPress={() => {
                                    this.refs.imageViewer.showViewer(index);
                                }}>
                                    <Image
                                        key={index}
                                        source={{uri: `http://202.120.40.8:30382/online-edu/static/${item}`}}
                                        style={{width: imgWidth, height: imgWidth * imgSize[index]}}
                                        onLoadStart={() => {
                                            Image.getSize(`http://202.120.40.8:30382/online-edu/static/${item}`, (width, height) => {
                                                let temp = this.state.imgSize;
                                                temp[index] = height/width;
                                                this.setState({
                                                    imgSize: temp
                                                })
                                            }, () => {
                                                console.log("fuck");
                                            })}
                                        }
                                    />
                                </TouchableOpacity>)
                            })
                        }
                        </Body>
                    </CardItem>
                    <CardItem>
                        <Left/>
                        <Right>
                            <Button transparent onPress={() => {this.props.navigation.navigate("CourseAddResponse",{
                                path: topic.path
                            })}}>
                                <Text>添加回复</Text>
                            </Button>
                        </Right>
                    </CardItem>
                </View>
                <ImageViewer ref={"imageViewer"} imgList={this.state.imgList}/>
            </View>
        );
    }
}

function mapStateToProps() {
    return {}
}

const mapDispatchToProps = dispatch => {
    return {
        setCurrentTopic: (topic) => dispatch(setCurrentTopic(topic))
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(CourseForumTopic);
