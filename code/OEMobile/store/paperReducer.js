import { SET_ANSWER, INIT_ANSWER } from "./paperAction"

const initAnswer = {
    answerMap: new Map()
};

function paperReducer(state = initAnswer, action) {
    switch (action.type) {
        case SET_ANSWER:
            var changeAnswer = new Map(state.answerMap);
            changeAnswer.set(action.newAnswer.questionId, action.newAnswer.answer);
            // console.log(action);
            // console.log(changeAnswer.keys());
            return {
                answerMap: changeAnswer
            };
        case INIT_ANSWER:
            return {
                answerMap: action.answerMap
            };
        default:
            return state;
    }
}

export default {
    paperAnswer: paperReducer
};
