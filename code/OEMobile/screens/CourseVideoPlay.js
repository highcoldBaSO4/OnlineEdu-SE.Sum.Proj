import React, {Component} from 'react';
import Video from 'react-native-video-controls';
import { Container } from "native-base";
import { StyleSheet } from "react-native"

var styles = StyleSheet.create({
    backgroundVideo: {
        position: 'absolute',
        top: 0,
        left: 0,
        bottom: 0,
        right: 0,
    },
});

class CourseVideoPlay extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        const videoUrl = this.props.navigation.getParam("videoUrl", "");

        return (
            <Container>
                <Video
                    ref={(ref: Video) => { //方法对引用Video元素的ref引用进行操作
                        this.video = ref
                    }}
                    source={{ uri: videoUrl }}
                    style={styles.backgroundVideo}
                    controls={true}
                    onBack={() => {this.props.navigation.pop()}}
                />
            </Container>
        );
    }
}

export default CourseVideoPlay;
