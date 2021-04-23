package com.se231.onlineedu.message.response;

import java.util.List;
import java.util.Map;
import com.se231.onlineedu.model.Course;
import com.se231.onlineedu.model.User;

/**
 * Grade Table Entity
 *
 * this entity is used to send response for listing course
 *
 * @author Zhe Li
 * @date 2019/07/26
 */
public class GradeTable {
    private Course course;

    List<StudentAndGrade> scoreMap;

    public GradeTable() {
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<StudentAndGrade> getScoreMap() {
        return scoreMap;
    }

    public void setScoreMap(List<StudentAndGrade> scoreMap) {
        this.scoreMap = scoreMap;
    }
}
