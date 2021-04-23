package com.se231.onlineedu.model;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Embeddable
public class LearnPrimaryKey implements Serializable {

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User student;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    public LearnPrimaryKey(User student, Course course) {
        this.student = student;
        this.course = course;
    }

    public LearnPrimaryKey() {
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LearnPrimaryKey that = (LearnPrimaryKey) o;
        return Objects.equals(student, that.student) &&
                Objects.equals(course, that.course);
    }

    @Override
    public int hashCode() {
        return Objects.hash(student, course);
    }
}
