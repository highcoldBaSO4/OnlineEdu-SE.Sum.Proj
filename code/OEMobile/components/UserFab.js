import React, {Component} from 'react';
import { Fab, Icon } from "native-base";

class UserFab extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <Fab position={"bottomRight"} onPress={() => {this.props.navigation.navigate("Home")}}>
                <Icon name={"user"} type={"FontAwesome"}/>
            </Fab>
        );
    }
}

export default UserFab;
