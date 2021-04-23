package com.se231.onlineedu.model;


import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class SignInPrimaryKey implements Serializable {
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    private int signInNo;

    public SignInPrimaryKey(Course course, int signInNo) {
        this.course = course;
        this.signInNo = signInNo;
    }

    public SignInPrimaryKey() {
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public int getSignInNo() {
        return signInNo;
    }

    public void setSignInNo(int signInNo) {
        this.signInNo = signInNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SignInPrimaryKey that = (SignInPrimaryKey) o;
        return signInNo == that.signInNo &&
                course.getId().equals(that.course.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(course.getId(), signInNo);
    }
}
