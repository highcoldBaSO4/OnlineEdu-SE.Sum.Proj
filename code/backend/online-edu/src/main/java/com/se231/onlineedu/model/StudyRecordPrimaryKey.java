package com.se231.onlineedu.model;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

/**
 * @author Zhe Li
 * @date 2019/09/05
 */
@Embeddable
public class StudyRecordPrimaryKey implements Serializable {
    private static final Long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    private Date studyDate;

    public StudyRecordPrimaryKey() {
    }

    public StudyRecordPrimaryKey(User user,Course course) {
        this.user = user;
        this.course = course;
        studyDate = new Date(new java.util.Date().getTime());
    }

    public StudyRecordPrimaryKey(User user, Course course,Date studyDate) {
        this.user = user;
        this.course = course;
        this.studyDate = studyDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getStudyDate() {
        return studyDate;
    }

    public void setStudyDate(Date studyDate) {
        this.studyDate = studyDate;
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
        StudyRecordPrimaryKey that = (StudyRecordPrimaryKey) o;
        return user.equals(that.user) &&
                course.equals(that.course) &&
                studyDate.equals(that.studyDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, course, studyDate);
    }
}
