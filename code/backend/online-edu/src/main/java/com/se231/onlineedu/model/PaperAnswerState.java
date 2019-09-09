package com.se231.onlineedu.model;

/**
 * Paper Answer State Enum
 *
 * enum of paper answer state
 *
 * @author Zhe Li
 * @date 2019/07/15
 */
public enum PaperAnswerState {
    /**
     * student hasn't answer the paper yet.
     */
    NOT_START,

    /**
     * save subjective questions' answers temporarily
     */
    TEMP_SAVE,
    /**
     * student start to answer but not finish(just save temporarily).
     */
    NOT_FINISH,
    /**
     * student has finished the paper and the paper's ddl hasn't come(thus no mark)
     */
    FINISHED,
    /**
     * the ddl of paper has come and teaching admin hasn't marked the paper yet.
     */
    NOT_MARKED,
    /**
     * the paper is marked.
     */
    MARKED
}
