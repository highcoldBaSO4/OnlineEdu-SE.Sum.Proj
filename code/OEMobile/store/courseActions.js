export const SET_COURSE_ID = "SET_COURSEID";
export const SET_COURSE_INFO = "SET_COURSE_INFO";
export const SET_COURSE_PAPERLIST = "SET_COURSE_PAPERLIST";

export function setPaperList(paperList) {
    return {
        type: SET_COURSE_PAPERLIST,
        newPaperList: paperList
    }
}
