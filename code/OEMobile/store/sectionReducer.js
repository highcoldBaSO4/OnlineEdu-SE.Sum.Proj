import { SET_CURRENT_SECTION } from "./sectionAction";

const initSection = {
    description: "",
    resources: [],
    papers: []
};

function sectionAction(state = initSection, action) {
    switch (action.type) {
        case SET_CURRENT_SECTION:
            return action.section;
        default:
            return state;
    }
}

export default {
    currentSection: sectionAction
}
