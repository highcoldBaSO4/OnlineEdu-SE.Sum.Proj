import React, {Component} from 'react';
import {Content, H3, Left, ListItem, Text} from "native-base";
import {Image} from "react-native";

class AssignmentMulti extends Component {
    constructor(props) {
        super(props);
        this.state = {
            answer: "",
            selectedList: this.props.initAnswer.split("")
        }
    }

    selectOption = (optionKey) => {
        let newSelectedList = this.state.selectedList;
        if (this.optionSelected(optionKey)) {
            newSelectedList.splice(newSelectedList.indexOf(optionKey), 1);
        }
        else {
            newSelectedList.push(optionKey);
        }
        newSelectedList.sort();
        console.log(newSelectedList);
        this.setState({
            answer: this.answerString(newSelectedList),
            selectedList: newSelectedList
        });
        let newAnswer = {
            questionId: this.props.question.id,
            answer: this.state.answer
        };
        this.props.setAnswer(newAnswer);
    };

    optionSelected = (optionKey) => {
        if (this.state.selectedList.indexOf(optionKey) === -1) return false;
        else return true;
    };

    answerString = (selectedList) => {
        let answer = "";
        for (let i of selectedList) {
            answer += i;
        }
        return answer;
    };

    _renderOption(optionKey) {
        return (
            <ListItem selected={this.optionSelected(optionKey)} onPress={() => this.selectOption(optionKey)}>
                <Text>{optionKey + ". " + this.props.question.options[optionKey]}</Text>
            </ListItem>
        )
    }

    render() {
        let imgWidth = this.$window.width;
        let imgSize = this.props.imgSize;

        return (
            <Content>
                <ListItem>
                    <Left>
                        <H3>多选题</H3>
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

                                })}
                            }
                        />
                    })
                }
                {/*<FlatList data={Object.keys(this.props.question.options)} renderItem={({item}) => this._renderOption(item)} />*/}
                {
                    Object.keys(this.props.question.options).map((item, index) => {
                        return this._renderOption(item);
                    })
                }
            </Content>
        );
    }
}

export default AssignmentMulti;
