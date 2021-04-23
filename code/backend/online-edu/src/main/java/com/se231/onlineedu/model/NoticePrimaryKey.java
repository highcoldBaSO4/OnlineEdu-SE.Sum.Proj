package com.se231.onlineedu.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author yuxuanLiu
 * @date 2019/07/22
 */
@Embeddable
public class NoticePrimaryKey implements Serializable {

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    private int noticeNo;

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public int getNoticeNo() {
        return noticeNo;
    }

    public void setNoticeNo(int noticeNo) {
        this.noticeNo = noticeNo;
    }

    public NoticePrimaryKey() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoticePrimaryKey that = (NoticePrimaryKey) o;
        return noticeNo == that.noticeNo &&
                Objects.equals(course, that.course);
    }

    @Override
    public int hashCode() {
        return Objects.hash(course, noticeNo);
    }
}
