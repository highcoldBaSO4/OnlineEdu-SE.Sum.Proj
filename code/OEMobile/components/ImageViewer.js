import React, {Component} from 'react';
import { Modal } from 'react-native';
import RNFetchBlob from "rn-fetch-blob";
import ImgViewer from "react-native-image-zoom-viewer"


class ImageViewer extends Component {
    constructor(props) {
        super(props);
        this.state = {
            showViewer: false,
            currentImg: 0
        }
    }

    saveImage = (url) => {
        RNFetchBlob.config({
            path: `${RNFetchBlob.fs.dirs.DownloadDir}/oeMobile/images`
        }).fetch('GET', url, {

        }).progress((received, total) => {

        }).then(() => {
            this.$toast.successToast("图片保存成功！");
        }).catch((error) => {
            this.$toast.errorToast("出错啦！" + error);
        })
    };

    showViewer = (index) => {
        this.setState({
            showViewer: true,
            currentImg: index
        })
    };

    hideViewer = () => {
        this.setState({
            showViewer: false
        })
    };

    render() {
        return (
            <Modal visible={this.state.showViewer} transparent animationType={"fade"}>
                <ImgViewer
                    imageUrls={this.props.imgList}
                    index={this.state.currentImg}
                    enableSwipeDown={true}
                    onClick={() => {
                        console.log(this.state.imgList);
                        this.setState({showViewer: false});
                    }}
                    menuContext={{
                        saveToLocal: "保存图片",
                        cancel: "取消"
                    }}
                    onSave={(url) => {
                        this.saveImage(url)
                    }}
                />
            </Modal>
        );
    }
}

export default ImageViewer;
