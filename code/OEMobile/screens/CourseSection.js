import React, {Component} from 'react';
import { FlatList } from "react-native";
import { Container, ListItem, Text } from "native-base";
import { connect } from "react-redux";
import ResourceLine from "../components/ResourceLine";
import PaperLine from "../components/PaperLine";

class CourseSection extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <Container>
                <ListItem itemDivider>
                    <Text>章节简介</Text>
                </ListItem>
                <ListItem>
                    <Text>
                        { this.props.section.description }
                    </Text>
                </ListItem>
                <FlatList
                    data={this.props.section.resources}
                    renderItem={({ item }) => <ResourceLine resourceInfo={item} navigation={this.props.navigation}/>}
                    ListHeaderComponent={() => (
                        <ListItem itemDivider>
                            <Text>章节资源</Text>
                        </ListItem>
                    )}
                    ListFooterComponent={() => (
                        <FlatList
                            data={this.props.section.papers}
                            renderItem={({ item }) => <PaperLine paper={item} navigation={this.props.navigation}/>}
                            ListHeaderComponent={() => (
                                <ListItem itemDivider>
                                    <Text>章节作业</Text>
                                </ListItem>
                            )}
                        />
                    )}
                />
            </Container>
        );
    }
}

function mapStateToProps(state) {
    return {
        section: state.currentSection
    }
}

export default connect(mapStateToProps)(CourseSection);
