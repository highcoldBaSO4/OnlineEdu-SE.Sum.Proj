import React, {Component} from 'react';
import { connect } from "react-redux";
import { Image } from "react-native";
import { Content, ListItem, Text, H3, Left } from 'native-base';
import {setAnswer} from "../store/paperAction";

class AssignmentSingle extends Component {
    constructor(props) {
        super(props);
        let initSize = new Array(this.props.question.images.length);
        initSize.fill(1);
        this.state = {
            answer: this.props.initAnswer,
            //answer: this.props.question.myAnswer,
            imgSize: initSize
        };
        //console.log(this.state.answer);
    }

    componentDidMount(): void {
        //console.log(this.props.question);
    }

    selectOption(select) {
        this.setState({
            answer: select
        });
        let newAnswer = {
            questionId: this.props.question.id,
            answer: select
        };
        this.props.setAnswer(newAnswer);
    }

    _renderOption(optionKey, index) {
        return (
            <ListItem selected={optionKey === this.state.answer} onPress={() => this.selectOption(optionKey)} key={index}>
                <Text>{optionKey + ". " + this.props.question.options[optionKey]}</Text>
            </ListItem>
        )
    }

    render() {
        let imgWidth = this.$window.width;
        let imgSize = this.state.imgSize;

        return (
            <Content>
                <ListItem>
                    <Left>
                        <H3>单选题</H3>
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
                {/*<FlatList data={Object.keys(this.props.question.options)} renderItem={({item}) => this._renderOption(item)} />*/}
                {
                    Object.keys(this.props.question.options).map((item, index) => {
                        return this._renderOption(item, index)
                    })
                }
            </Content>
        );
    }
}

const mapDispatchToProps = (dispatch) => {
    return {
        //setAnswer: (newAnswer) => dispatch(setAnswer(newAnswer))
    }
};

export default connect(null, mapDispatchToProps)(AssignmentSingle);
