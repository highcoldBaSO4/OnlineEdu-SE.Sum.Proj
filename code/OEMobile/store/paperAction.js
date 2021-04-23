export const SET_ANSWER = "ADD_ANSWER";
export const INIT_ANSWER = "INIT_ANSWER";

export function setAnswer(newAnswer) {
    return {
        type: SET_ANSWER,
        newAnswer: newAnswer
    }
}

export function initAnswer(answerMap) {
    return {
        type: INIT_ANSWER,
        answerMap: answerMap
    }
}
