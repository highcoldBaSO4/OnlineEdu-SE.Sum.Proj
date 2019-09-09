import React, {Component} from 'react';
import { Image } from "react-native";
import { Content, ListItem, Text, H3, Left } from 'native-base';

class AssignmentJudge extends Component {
    constructor(props) {
        super(props);
        this.state = {
            answer: this.props.initAnswer,
        }
    }

    makeChoice = (choice) => {
        this.setState({
            answer: choice
        });
        let newAnswer = {
            questionId: this.props.question.id,
            answer: choice
        };
        this.props.setAnswer(newAnswer);
    };

    render() {
        let imgWidth = this.$window.width;
        let imgSize = this.props.imgSize;

        return (
            <Content>
                <ListItem>
                    <Left>
                        <H3>判断题</H3>
                    </Left>
                </ListItem>
                <ListItem>
                    <Text>{this.props.question.content}</Text>
                </ListItem>
                {
                    this.props.question.images.map((item, index) => {
                        return <Image
                            source={{uri: `http://202.120.40.8:30382/online-edu/static/${item}`}}
                            key={index}
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
                    })
                }
                <ListItem onPress={() => {this.makeChoice("T")}} selected={this.state.answer === "T"}>
                    <Text>正确</Text>
                </ListItem>
                <ListItem onPress={() => {this.makeChoice("F")}} selected={this.state.answer === "F"}>
                    <Text>错误</Text>
                </ListItem>
            </Content>
        );
    }
}

export default AssignmentJudge;
