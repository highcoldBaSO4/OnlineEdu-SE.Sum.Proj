import React, { Component } from "react";
import { Container, Button, Form, Item, Input, Label, Text } from 'native-base';
import { connect } from "react-redux";
import UserEmailConfirm from '../components/UserEmailConfirm';

class UserEmailSetting extends Component {
    constructor(props) {
        super(props);
        this.state = {
            newEmail: this.props.userEmail,
            showConfirm: false,
        }
    }

    changeRequest = () => {
        global.showLoading("获取验证码中");
        this.$axios.request({
            url: this.props.userUrl + "email",
            method: "patch",
            data: {
                email: this.state.newEmail
            },
            headers: this.props.authHeader
        }).then(() => {
            this.$toast.successToast("已发送验证码");
            this.setState({showConfirm: true})
        }).catch((error) => {
            this.$toast.errorToast("验证码获取失败");
            console.log(error.response);
        })
    };

    sendConfirm = (confirmCode) => {
        this.$axios.request({
            url: this.props.userUrl + "email/confirm",
            method: "get",
            params: {
                verificationToken: confirmCode
            },
            headers: this.props.authHeader,
            withCredentials: true
        }).then(() => {
            this.$toast.successToast("修改成功！");
            this.setState({showConfirm: false});
        }).catch((error) => {
            console.log(error.response);
            this.$toast.errorToast("验证码出错");
        })
    };

    render() {
        if (this.state.showConfirm) {
            return (
                <UserEmailConfirm sendConfirm={this.sendConfirm}/>
            )
        }
        else {
            return (
                <Container>
                    <Form>
                        <Item stackedLabel>
                            <Label>修改邮箱</Label>
                            <Input defaultValue={this.state.newEmail} onChangeText={(text) => this.setState({newEmail: text})}/>
                        </Item>
                    </Form>
                    <Button full onPress={() => {this.changeRequest()}}>
                        <Text>提交修改</Text>
                    </Button>
                </Container>
            );
        }
    }
}

function mapStateToProps(state) {
    return {
        accessToken: state.login.accessToken,
        userEmail: state.userInfo.email,
        userUrl: "/api/users/" + state.userInfo.id + "/",
        authHeader: state.login.authHeader
    }
}



const mapDispatchToProps = dispatch => {
    return {}
};

export default connect(mapStateToProps, mapDispatchToProps)(UserEmailSetting);
