import {SET_COURSE_INFO, SET_COURSE_PAPERLIST} from "./courseActions";

const courseInfoInit = {
    avatarUrl: "",
    courseTitle: "",
    endDate:"2019-08-09T16:00:00.000+0000",
    id: 8,
    learns: [],
    location: "",
    notices: [],
    papers: [],
    sectionList: [],
    signIns: [],
    startDate: [],
    state: "",
    students: [],
    teacher: {},
    teacherAssistants: [],
    timeSlots: [],
};

function courseInfoAction(state = courseInfoInit, action) {
    switch (action.type) {
        case SET_COURSE_INFO:
            return action.courseInfo;
        case SET_COURSE_PAPERLIST:
            return Object.assign({}, state, action.newPaperList);
        default:
            return state;
    }
}

export default {
    courseInfo: courseInfoAction
}
