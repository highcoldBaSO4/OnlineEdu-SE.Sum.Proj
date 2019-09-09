export const SET_CURRENT_TOPIC = "SET_CURRENT_TOPIC";

export function setCurrentTopic(topic) {
    return {
        type: SET_CURRENT_TOPIC,
        topic: topic
    }
}
