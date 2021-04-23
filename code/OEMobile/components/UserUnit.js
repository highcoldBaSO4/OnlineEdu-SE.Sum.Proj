import React, { Component } from "react";
import { Text, View, StyleSheet } from "react-native";
import { Thumbnail } from 'native-base';

const styles = StyleSheet.create({
    mainView: {
        flexDirection: "row"
    },
    userName: {
        fontSize: 13,
        bottom: 0,
        marginTop: 10
    }
});

class UserUnit extends Component {
    constructor(props) {
        super(props);
        this.state = {
            user: {
                username: "",
                avatarUrl: ""
            }
        }
    }

    componentWillMount(): void {
        let temp = this.props.user;
        if (typeof temp === "number") {
            this.$axios.request({
                url: `/api/users/${temp}/avatar`,
                method: "get"
            }).then((response) => {
                console.log(response.data);
                this.setState({
                    user: {
                        username: response.data.username,
                        avatarUrl: response.data.avatar
                    }
                })
            }).catch((error) => {
                console.log(error);
                alert(error);
            })
        }
        else {
            this.setState({
                user: this.props.user
            })
        }
    }

    render() {
        return (
            <View style={styles.mainView}>
                <Thumbnail style={{width: 25, height: 25}} source={{uri:"http://202.120.40.8:30382/online-edu/static/" + this.state.user.avatarUrl}}/>
                <Text style={styles.userName}>{this.state.user.username}</Text>
            </View>
        )
    }
}

export default UserUnit;
