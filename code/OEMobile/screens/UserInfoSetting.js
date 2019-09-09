import React, {Component} from 'react';
import { Container, Form, Item, Label, Text, Button, Input, Picker } from 'native-base';
import {connect} from 'react-redux';

class UserInfoSetting extends Component {
    constructor(props) {
        super(props);
        this.state = {
            realName: this.props.userInfo.realName,
            major: this.props.userInfo.major,
            grade: this.props.userInfo.grade,
            sex: this.props.userInfo.sex,
            sno: this.props.userInfo.sno,
            tel: this.props.userInfo.tel,
            university: this.props.userInfo.university,
        }
    }

    changeRequest = () => {
        global.showLoading("提交修改中");
        this.$axios.request({
            url: "/api/users/info/modify",
            method: "post",
            data: this.state,
            headers: {
                "Authorization": "Bearer " + this.props.accessToken
            },
        }).then(() => {
            alert("修改成功！");
            this.props.setUserInfo(this.state);
        }).catch((error) => {
            console.log(error.response);
            alert("出错啦");
        })
    }

    render() {
        return (
            <Container>
                <Form>
                    <Item stackedLabel>
                        <Label>真实姓名</Label>
                        <Input defaultValue={this.state.realName} onChangeText={(text) => this.setState({realName: text})}/>
                    </Item>
                    <Item stackedLabel>
                        <Label>手机号</Label>
                        <Input defaultValue={this.state.tel} onChangeText={(text) => this.setState({tel: text})}/>
                    </Item>
                    <Item>
                        <Label>性别</Label>
                        <Picker
                            note
                            mode={"dropdown"}
                            selectedValue={this.state.sex}
                            onValueChange={(value) => {
                                this.setState({sex: value});
                            }}
                        >
                            <Picker.Item label={"未知"} value={""} />
                            <Picker.Item label={"男"} value={"男"} />
                            <Picker.Item label={"女"} value={"女"} />
                        </Picker>
                    </Item>
                    <Item stackedLabel>
                        <Label>学校</Label>
                        <Input defaultValue={this.state.university} onChangeText={(text) => this.setState({university: text})}/>
                    </Item>
                    <Item stackedLabel>
                        <Label>学号</Label>
                        <Input defaultValue={this.state.sno} onChangeText={(text) => this.setState({sno: text})}/>
                    </Item>
                    <Item stackedLabel>
                        <Label>年级</Label>
                        <Input onChangeText={(text) => this.setState({realName: text})}/>
                    </Item>
                    <Item stackedLabel>
                        <Label>专业</Label>
                        <Input defaultValue={this.state.major} onChangeText={(text) => this.setState({major: text})}/>
                    </Item>
                </Form>
                <Button full onPress={() => {this.changeRequest()}}>
                    <Text>确认修改</Text>
                </Button>
            </Container>
        );
    }
}

function mapStateToProps(state) {
    return {
        accessToken: state.login.accessToken,
        userInfo: state.userInfo,
        userUrl: "/api/users/" + state.userInfo.id + "/"
    }
}

const mapDispatchToProps = dispatch => {
    return {
        setUserInfo: (userInfo) => dispatch({
            type: "SET_USERINFO",
            userInfo
        })
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(UserInfoSetting);
