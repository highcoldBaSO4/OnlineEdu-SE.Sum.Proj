import React, {Component} from 'react';
import {View, Card, CardItem, Text, Left, Right, Body, Button} from "native-base";
import UserUnit from "./UserUnit";
import {Image, TouchableOpacity} from "react-native";
import ImageViewer from "./ImageViewer";

class CourseForumResponseUnit extends Component {
    constructor(props) {
        super(props);
        let imgLength = this.props.response.imageUrls.length;
        let initSize = new Array(imgLength);
        initSize.fill(1);
        let imgList = [];
        for (let i of this.props.response.imageUrls) {
            imgList.push({
                url: `http://202.120.40.8:30382/online-edu/static/${i}`
            })
        }
        console.log(imgList);
        this.state = {
            imgSize: initSize,
            showViewer: false,
            currentImg: 0,
            showResponse: false,
            imgList
        }
    }

    render() {
        let response = this.props.response;
        let imgWidth = this.$window.width;
        let imgSize = this.state.imgSize;

        return (
            <View>
                <Card>
                    <CardItem header bordered>
                        <Left>
                            <UserUnit user={response.userId}/>
                        </Left>
                        <Right>
                            <Text note>{response.createdAt}</Text>
                        </Right>
                    </CardItem>
                    <CardItem>
                        <Text>回复：</Text>
                        <UserUnit user={response.responseTo}/>
                    </CardItem>
                    <CardItem>
                        <Body>
                        <Text>{response.content}</Text>
                        {
                            response.imageUrls.map((item, index) => {
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
                        <ImageViewer ref={"imageViewer"} imgList={this.state.imgList}/>
                    </CardItem>
                    <CardItem footer>
                        <Left>
                            <Button transparent onPress={() => {this.setState({showResponse: !this.state.showResponse})}}>
                                <Text>{this.state.showResponse ? "隐藏回复" : "显示回复"}</Text>
                            </Button>
                        </Left>
                        <Right>
                            <Button transparent onPress={() => {this.props.addResponse(response.path)}}>
                                <Text>添加回复</Text>
                            </Button>
                        </Right>
                    </CardItem>
                </Card>
                {
                    this.state.showResponse ? response.responses.map((item) => <CourseForumResponseUnit response={item} addResponse={this.props.addResponse} />) : <View/>
                }
            </View>
        );
    }
}

export default CourseForumResponseUnit;
