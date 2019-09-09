package com.se231.onlineedu.model;

/**
 * Course State Enum
 *
 * enum of course state
 *
 * @author Zhe Li
 *
 * @date 2019/7/4
 */
public enum CourseState {
    /**
     * teacher admin has committed the application but haven't got answer yet.
     */
    APPLYING,

    /**
     * the course has been passed by higher admin but is not started yet.
     */
    READY_TO_START,

    /**
     * the teaching is teaching the course.
     */
    TEACHING,

    /**
     * the course has finished.
     */
    FINISHED,

    /**
     * higher admin has denied the application.
     */
    NOT_PASS

}
