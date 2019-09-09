package com.se231.onlineedu.message.response;

import com.se231.onlineedu.model.Course;

/**
 * @author liu
 */
public class CourseWithIdentity {
    private Identity identity;

    private Course course;

    public CourseWithIdentity() {
    }

    public CourseWithIdentity(Identity identity, Course course) {
        this.identity = identity;
        this.course = course;
    }

    public Identity getIdentity() {
        return identity;
    }

    public void setIdentity(Identity identity) {
        this.identity = identity;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
