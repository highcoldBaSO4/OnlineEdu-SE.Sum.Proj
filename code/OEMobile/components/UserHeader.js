import React, {Component} from 'react';
import {Header, Text, Icon, Right, Button, Left, Thumbnail, Body, Title} from 'native-base';
import { connect } from "react-redux";

class UserHeader extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <Header>
                <Left>
                    <Thumbnail small source={{uri: "http://202.120.40.8:30382/online-edu/static/" + this.props.avatarUrl}} style={{borderStyle: "solid"}}/>
                </Left>
                <Body>
                <Title>
                    {this.props.title}
                </Title>
                </Body>
                <Right>
                    <Button icon transparent onPress={() => {this.props.navigation.navigate("Login")}}>
                        <Icon name={"sign-out"} type={"FontAwesome"}/>
                    </Button>
                </Right>
            </Header>
        );
    }
}

function mapStateToProps(state) {
    return {
        avatarUrl: state.userInfo.avatarUrl
    }
}

export default connect(mapStateToProps, null)(UserHeader);
