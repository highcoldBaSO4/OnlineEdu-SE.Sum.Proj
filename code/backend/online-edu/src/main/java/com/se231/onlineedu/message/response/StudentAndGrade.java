package com.se231.onlineedu.message.response;

import com.se231.onlineedu.model.User;

/**
 * @author Zhe Li
 * @date 2019/07/26
 */
public class StudentAndGrade {
    private User student;

    private Double grade;

    public StudentAndGrade() {
    }

    public StudentAndGrade(User student, Double grade) {
        this.student = student;
        this.grade = grade;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public Double getScore() {
        return grade;
    }

    public void setScore(Double grade) {
        this.grade = grade;
    }
}
