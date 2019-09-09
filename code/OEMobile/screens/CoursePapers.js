import React, {Component} from 'react';
import { FlatList } from "react-native";
import { ListItem } from "native-base";
import { connect } from "react-redux";

class CoursePapers extends Component {
    constructor(props) {
        super(props);
        this.state = {
            paperList: []
        }
    }

    loadPaperList = () => {
        this.$axios.request({

        })
    };

    render() {
        return (

        );
    }
}

function mapStateToProps(state) {
    return {
        courseId: state.courseInfo
    }
}

export default CoursePapers;
