package com.se231.onlineedu.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * SectionRepository Primary Key Class
 *
 * a section must be identified by a course an its no
 *
 * @author Yuxuan Liu
 *
 * @date 2019/7/4
 */
@Embeddable
public class SectionPrimaryKey implements Serializable {
    private static final Long serialVersionUID = 1L;

    @JsonBackReference
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int secId;

    public SectionPrimaryKey(Course course,int secId){
        this.course=course;
        this.secId=secId;
    }

    public SectionPrimaryKey() {
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public int getSecId() {
        return secId;
    }

    public void setSecId(int secId) {
        this.secId = secId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SectionPrimaryKey that = (SectionPrimaryKey) o;
        return secId == that.secId &&
                Objects.equals(course, that.course);
    }

    @Override
    public int hashCode() {
        return Objects.hash(course, secId);
    }
}
