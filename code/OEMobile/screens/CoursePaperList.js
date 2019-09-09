import React, {Component} from 'react';
import { FlatList } from "react-native";
import { Container, ListItem, Text } from "native-base";
import { connect } from 'react-redux';
import PaperLine from "../components/PaperLine";
import { setPaperList } from "../store/courseActions";

class CoursePaperList extends Component {
    constructor(props) {
        super(props);
    }

    componentWillMount(): void {
        global.showLoading("获取作业列表中");
        this.$axios.request({
            url: '/api/courses/'+ this.props.courseId +'/papers/state',
            method: "get",
            headers: this.props.authHeader
        }).then((response) => {
            let paperList = this.response.data;
            paperList.sort((a, b) => {
                if (a.end < b.end) {
                    return -1;
                }
                else if (a.end === b.end) {
                    if (a.start < b.start) {
                        return -1;
                    }
                    else return 1;
                }
                else return 1;
            });
            this.props.setPaperList(paperList);
        })
    }

    render() {
        return (
            <Container>
                <FlatList
                    renderItem={({ item }) => <PaperLine paper={item} navigation={this.props.navigation}/>}
                    data={this.props.papers}
                    ListHeaderComponent={() => (
                        <ListItem itemDivider>
                            <Text>
                                课程作业
                            </Text>
                        </ListItem>
                    )}
                />
            </Container>
        );
    }
}

function mapStateToProps(state) {
    return {
        authHeader: state.login.authHeader,
        courseId: state.courseInfo.id,
        papers: state.courseInfo.papers
    }
}

const mapDispatchToProps = (dispatch) => {
    return {
        setPaperList: (paperList) => dispatch(setPaperList(paperList))
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(CoursePaperList);
