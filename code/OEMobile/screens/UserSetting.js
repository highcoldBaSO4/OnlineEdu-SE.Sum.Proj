import React from "react";
import { List, ListItem, Left, Right, View, Text } from 'native-base';
import { connect } from "react-redux";
import Icon from 'react-native-vector-icons/FontAwesome';
import ImagePicker from 'react-native-image-picker';
import { setNewAvatar } from "../store/actions";

class UserSetting extends React.Component {
    constructor(props) {
        super(props);
        //this.onSelectMenu = this.onSelectMenu.bind(this);
    }

    imagePickerOptions = {
        title: "选择新头像",
        cancelButtonTitle: "取消",
        takePhotoButtonTitle: "打开相机",
        chooseFromLibraryButtonTitle: "从相册选取"
    };

    selectedNewAvatar = (response) => {
        console.log(response);
        if (!response.didCancel) {
            if (response.error) {
                alert(response.error);
            }
            else {
                global.showLoading("上传新头像中");
                let avatarFile = new FormData();
                console.log(response.uri);
                avatarFile.append("avatar", {
                    uri: response.uri,
                    type: response.type,
                    name: response.fileName
                });
                this.$axios.request({
                    url: "/api/users/" + this.props.userId + "/avatar",
                    method: "post",
                    data: avatarFile,
                    headers: {
                        ...this.props.authHeader,
                        'Content-Type': 'multipart/form-data'
                    }
                }).then((response) => {
                    //console.log(response);
                    this.$toast.successToast("修改成功");
                    this.newUrl = response.data;
                    this.props.setNewAvatar(this.newUrl);
                }).catch((error) => {
                    this.$toast.errorToast("修改失败");
                    console.log(error.response);
                })
            }
        }
    };

    render() {
        const onSelectMenu = (target) => {
            this.props.navigation.navigate(target)
        };

        return (
            <View>
                <List>
                    <ListItem button onPress={() => {ImagePicker.showImagePicker(this.imagePickerOptions, this.selectedNewAvatar)}}>
                        <Left>
                            <Text>修改头像</Text>
                        </Left>
                        <Right>
                            <Icon name={"chevron-right"} />
                        </Right>
                    </ListItem>
                    <ListItem button onPress={() => {onSelectMenu("UserPasswordSetting")}}>
                        <Left>
                            <Text>修改密码</Text>
                        </Left>
                        <Right>
                            <Icon name={"chevron-right"} />
                        </Right>
                    </ListItem>
                    <ListItem button onPress={() => {onSelectMenu("UserEmailSetting")}}>
                        <Left>
                            <Text>修改邮箱</Text>
                        </Left>
                        <Right>
                            <Icon name={"chevron-right"} />
                        </Right>
                    </ListItem>
                    <ListItem button onPress={() => {onSelectMenu("UserInfoSetting")}}>
                        <Left>
                            <Text>修改基本信息</Text>
                        </Left>
                        <Right>
                            <Icon name={"chevron-right"} />
                        </Right>
                    </ListItem>
                </List>
            </View>
        );
    }
}

function mapStateToProps(state) {
    return {
        authHeader: state.login.authHeader,
        userId: state.userInfo.id
    }
}

const mapDispatchToProps = (dispatch) => {
    return {
        setNewAvatar: (newAvatar) => dispatch(setNewAvatar(newAvatar))
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(UserSetting);
