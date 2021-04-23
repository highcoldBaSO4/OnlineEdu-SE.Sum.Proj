import React, {Component} from 'react';
import { Header, Left, Body, Button, Title, Icon, Right } from "native-base";
import { connect } from "react-redux";

class CourseHeader extends Component {
    constructor(props) {
        super(props);
    }

    showDrawer() {
        this.props.openDrawer();
    }

    render() {
        return (
            <Header>
                {/*<Left>*/}
                    {/*<Button transparent onPress={() => {this.props.navigation.openDrawer()}}>*/}
                        {/*<Icon name='menu' />*/}
                    {/*</Button>*/}
                {/*</Left>*/}
                <Body>
                    <Title>
                        { this.props.courseTitle }
                    </Title>
                </Body>
                <Right>
                    <Button icon transparent onPress={() => {this.props.navigation.navigate("Home")}}>
                        <Icon name={"user"} type={"FontAwesome"}/>
                    </Button>
                </Right>
            </Header>
        );
    }
}

function mapStateToProps(state) {
    return {
        courseTitle: state.courseInfo.courseTitle
    }
}

export default connect(mapStateToProps)(CourseHeader);
