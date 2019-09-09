import React, {Component} from 'react';
import {PermissionsAndroid} from 'react-native';
import { ListItem, Text, Icon, Left, Body, Button, Right} from "native-base";
import RNFetchBlob from "rn-fetch-blob";
import * as Progress from "react-native-progress";
import FileViewer from "react-native-file-viewer";

class ResourceLine extends Component {
    constructor(props) {
        super(props);
        this.state = {
            status: "undownloaded",
            downloadProgress: 0
        }
    }

    resourcePath = RNFetchBlob.fs.dirs.DownloadDir + "/oeMobile/" + this.props.resourceInfo.title;

    async requestStoragePermission() {
        try {
            const granted = await PermissionsAndroid.request(
                PermissionsAndroid.PERMISSIONS.READ_EXTERNAL_STORAGE
            );
            if (granted === PermissionsAndroid.RESULTS.GRANTED) {
                console.log('Read permission granted');
            } else {
                console.log('Camera permission denied');
            }
        } catch (err) {
            console.warn(err);
        }
    }

    async requestWritePermission() {
        try {
            const granted = await PermissionsAndroid.request(
                PermissionsAndroid.PERMISSIONS.WRITE_EXTERNAL_STORAGE
            );
            if (granted === PermissionsAndroid.RESULTS.GRANTED) {
                console.log('Write permission granted');
            } else {
                console.log('Camera permission denied');
            }
        } catch (err) {
            console.warn(err);
        }
    }

    componentWillMount(): void {
        this.requestStoragePermission();
        this.requestWritePermission();
        RNFetchBlob.fs.exists(this.resourcePath).then((exists) => {
            if (exists) {
                this.setState({
                    status: "downloaded"
                })
            }
            else {
                this.setState({
                    status: "undownloaded"
                });
            }
        })
    }

    _getResource() {
        let resourceUrl = "http://202.120.40.8:30382/online-edu/static/" + this.props.resourceInfo.url;
        let type = this.props.resourceInfo.resourceType;
        switch (type) {
            case "VIDEO":
                this.props.navigation.navigate("CourseVideoPlay",{
                    videoUrl: resourceUrl
                });
                break;
            default:
                RNFetchBlob.fs.exists(this.resourcePath).then((exists) => {
                    if (exists) {
                        this.setState({
                            status: "downloaded"
                        });
                        FileViewer.open(this.resourcePath).then(() => {
                            alert(">>");
                        }).catch((error) => {
                            console.log(error);
                            alert(error);
                        })
                        // Linking.openURL("file:///" + this.resourcePath).catch((err) => {
                        //     console.log(this,this.resourcePath);
                        //     console.log(err);
                        // })
                    }
                    else {
                        this.setState({
                            status: "downloading"
                        });
                        RNFetchBlob.config({
                            //fileCache: true,
                            path: this.resourcePath
                            // addAndroidDownloads: {
                            //     useDownloadManager: false,
                            //     //mime: typeMap[type],
                            // }
                        }).fetch('GET', resourceUrl,{

                        }).progress((received, total) => {
                            this.setState({
                                downloadProgress: received / total
                            })
                        }).then((res) => {
                            console.log(resourceUrl);
                            this.$toast.successToast("资源保存成功");
                            this.setState({
                                status: "downloaded"
                            })
                        }).catch((error) => {
                            alert(error);
                            this.setState({
                                status: "undownloaded"
                            })
                        });
                    }
                })
        }
    }

    _renderRight() {
        if (this.props.resourceInfo.resourceType === "VIDEO") {
            return <Icon name={"play"} />;
        }
        else {
            switch (this.state.status) {
                case "undownloaded":
                    return <Icon name={"download"} />;
                case "downloading":
                    return <Progress.Circle progress={this.state.downloadProgress}/>;
                case "downloaded":
                    return <Icon name={"folder"}/>;
                default:
                    return <Icon name={"what"}/>;
            }
        }
    }

    _iconType() {
        switch (this.props.resourceInfo.resourceType) {
            case "VIDEO": return "file-video-o";
            case "PPT": return "file-powerpoint-o";
            case "PDF": return "file-pdf-o";
            default: return "file-text-o";
        }
    }

    render() {
        return (
            <ListItem avatar onPress={() => this._getResource()}>
                <Left>
                    <Icon name={this._iconType()} type={"FontAwesome"} />
                </Left>
                <Body>
                    <Text>{this.props.resourceInfo.title}</Text>
                </Body>
                <Right>
                    {this._renderRight()}
                </Right>
            </ListItem>
        );
    }
}

export default ResourceLine;
