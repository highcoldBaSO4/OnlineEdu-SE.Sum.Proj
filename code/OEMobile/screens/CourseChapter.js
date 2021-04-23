import React, {Component} from 'react';
import { FlatList, } from "react-native";
import { Container, Content, ListItem, Text, View } from "native-base";
import { connect } from "react-redux";
import { setCurrentSection } from "../store/sectionAction";

class CourseChapter extends Component {
    constructor(props) {
        super(props);
    }

    moveToSection(section, chapterTitle) {
        this.props.setCurrentSection(section);
        this.props.navigation.navigate("CourseSection",{
            sectionTitle: chapterTitle +  ": " + section.title
        });
    }

    _drawChapter(chapter) {
        return (
            <View>
                <ListItem itemDivider>
                    <Text>{chapter.title}</Text>
                </ListItem>
                <FlatList renderItem={({item}) => this._drawSection(item, chapter.title)} data={chapter.sectionBranchesList} />
            </View>
        )
    }

    _drawSection(section, chapterTitle) {
        return (
            <ListItem onPress={() => {this.moveToSection(section, chapterTitle)}}>
                <Text>{section.title}</Text>
            </ListItem>
        )
    }

    render() {
        return (
            <Container>
                <Content>
                    <FlatList renderItem={({item}) => this._drawChapter(item)} data={this.props.chapters} />
                </Content>
            </Container>
        );
    }
}

function mapStateToProps(state) {
    return {
        chapters: state.courseInfo.sectionList
    }
}

const mapDispatchToProps = (dispatch) => {
    return {
        setCurrentSection: (section) => dispatch(setCurrentSection(section))
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(CourseChapter);
