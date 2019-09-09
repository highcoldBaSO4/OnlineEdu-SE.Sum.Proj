import React, {Component} from 'react';
import { View, Image, StyleSheet, Dimensions } from 'react-native';
import { connect } from 'react-redux';
import { Container, Text, H1 } from "native-base";
import UserUnit from './UserUnit';

const styles = StyleSheet.create({
    courseAvatar: {
        width: Dimensions.get('window').width,
        height: Dimensions.get('window').width * 0.618
    }
})

class CourseInfo extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <Container>
                <View style={{flex: 6}}>
                    <Image style={styles.courseAvatar} source={{uri: "http://202.120.40.8:30382/online-edu/static/" + this.props.courseInfo.avatarUrl}}/>
                </View>
                <View style={{flex: 9, flexDirection: 'row'}}>
                    <View style={{flex: 1}} />
                    <View style={{flex: 20}}>
                        <View style={{flex: 1}}>
                            <H1>{this.props.courseInfo.courseTitle}</H1>
                        </View>
                        <View style={{flex: 1, flexDirection: 'row'}}>
                            <View style={{flex: 1, flexDirection: 'row'}}>
                                <View>
                                    <Text>老师：</Text>
                                </View>
                                <View>
                                    <UserUnit user={this.props.courseInfo.teacher}/>
                                </View>
                            </View>
                            <View style={{flex: 1, flexDirection: 'row'}}>
                                <View>
                                    <Text>上课地点：</Text>
                                </View>
                                <View>
                                    <Text>{this.props.courseInfo.location}</Text>
                                </View>
                            </View>
                        </View>
                        <View style={{flex: 1, flexDirection: 'row'}}>
                            <View>
                                <Text>时间：</Text>
                            </View>
                            <View>
                                <Text>{this.props.courseInfo.startDate.substr(0,10) + " ~ " + this.props.courseInfo.endDate.substr(0,10)}</Text>
                            </View>
                        </View>
                        <View style={{flex: 4}}>
                            <Text>{this.props.courseInfo.coursePrototype.description}</Text>
                        </View>
                    </View>
                    <View style={{flex: 1}} />
                </View>
            </Container>
        );
    }
}

function mapStateToProps(state) {
    return {
        courseInfo: state.courseInfo,
    }
}

export default connect(mapStateToProps, null)(CourseInfo);
