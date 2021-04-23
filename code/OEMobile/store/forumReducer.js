import { SET_CURRENT_TOPIC } from "./forumActions";

const initTopic = {

};

function topicReducer(state = initTopic, action) {
    switch (action.type) {
        case SET_CURRENT_TOPIC:
            return action.topic;
        default:
            return state;
    }
}

export default {
    topic: topicReducer
}
