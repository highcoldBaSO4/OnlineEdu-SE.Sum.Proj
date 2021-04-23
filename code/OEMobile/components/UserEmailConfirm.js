import React, {Component} from 'react';
import { Container, H3, Form, Item, Label, Input, Button, Text } from 'native-base';

class UserEmailConfirm extends Component {
    constructor(props) {
        super(props);
        this.state = {
            confirmCode: ""
        };
    }

    render() {
        return (
            <Container>
                <Text>请输入发往邮箱的二维码</Text>
                <Form>
                    <Item>
                        <Label>验证码</Label>
                        <Input onChangeText={(text) => this.setState({confirmCode: text})}/>
                    </Item>
                </Form>
                <Button full onPress={() => {global.showLoading("验证审核中");this.props.sendConfirm(this.state.confirmCode)}}>
                    <Text>确认</Text>
                </Button>
            </Container>
        );
    }
}

export default UserEmailConfirm;
