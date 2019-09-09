package com.se231.onlineedu.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.sql.Date;

/**
 * This Entity is used to record the learning activity of student mainly in watching video.
 * @author Zhe Li
 * @date 2019/09/05
 */
@Entity
public class StudyRecord {
    @EmbeddedId
    private StudyRecordPrimaryKey studyRecordPrimaryKey;

    private int timeInMinute;

    private int pauseTime;

    private int changeSpeedTime;

    private int jumpTimes;

    public StudyRecord() {
    }

    public StudyRecord(User user, Course course, Date date) {
        studyRecordPrimaryKey=new StudyRecordPrimaryKey(user,course,date);
        timeInMinute=0;
        pauseTime=0;
        changeSpeedTime=0;
        jumpTimes=0;
    }

    public StudyRecordPrimaryKey getStudyRecordPrimaryKey() {
        return studyRecordPrimaryKey;
    }

    public void setStudyRecordPrimaryKey(StudyRecordPrimaryKey studyRecordPrimaryKey) {
        this.studyRecordPrimaryKey = studyRecordPrimaryKey;
    }

    public int getTimeInMinute() {
        return timeInMinute;
    }

    public void setTimeInMinute(int timeInMinute) {
        this.timeInMinute = timeInMinute;
    }

    public int getPauseTime() {
        return pauseTime;
    }

    public void setPauseTime(int pauseTime) {
        this.pauseTime = pauseTime;
    }

    public int getChangeSpeedTime() {
        return changeSpeedTime;
    }

    public void setChangeSpeedTime(int doubleSpeedTime) {
        this.changeSpeedTime = doubleSpeedTime;
    }

    public int getJumpTimes() {
        return jumpTimes;
    }

    public void setJumpTimes(int jumpTimes) {
        this.jumpTimes = jumpTimes;
    }
}
