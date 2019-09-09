import React, {Component} from 'react';
import { View } from "react-native";
import { ListItem, Left, Body, Right, Icon, Text } from "native-base";

class PaperLine extends Component {
    constructor(props) {
        super(props);
    }

    paperDDL() {
        return this.props.paper.end.substr(0,10) + " " + this.props.paper.end.substr(11,8)
    }

    render() {
        return (
            <ListItem avatar onPress={() => {
                this.props.navigation.navigate("CoursePaper", {
                    paperId: this.props.paper.paperId,
                    paper: this.props.paper
                })
            }}>
                <Left>
                    <Icon type={"FontAwesome"} name={"file-text-o"} />
                </Left>
                <Body>
                    <Text>   {this.props.paper.title}</Text>
                </Body>
                <Right>
                    <Text note>DDL: {this.paperDDL()}</Text>
                </Right>
            </ListItem>
        );
    }
}

export default PaperLine;
