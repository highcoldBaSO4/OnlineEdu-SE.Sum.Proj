package com.se231.onlineedu.model;

/**
 *  Course Prototype State Enum
 *
 *  enum of course prototype
 *
 * @author Zhe Li
 *
 * @date 2019/7/4
 */
public enum CoursePrototypeState {
    /**
     * a teaching admin apply to create a course prototype , but the admin and super admin haven't examine yet.
     */
    NOT_DECIDE,

    /**
     * this course type can be used.
     */
    USING,

    /**
     * admin of super admin deny the application
     */
    DENIAL
}
