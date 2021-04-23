import React, { Component } from "react";
import { Text } from "react-native";
import { Container, Button, Form, Item, Input, Label } from 'native-base';

class UserAvatarSetting extends Component {
    render() {
        return (
            <Container>
                <Form>
                    <Item stackedLabel>
                        <Label>修改邮箱</Label>
                        <Input/>
                    </Item>
                </Form>
                <Button>
                    <Text>提交修改</Text>
                </Button>
            </Container>
        );
    }
}

export default UserAvatarSetting
