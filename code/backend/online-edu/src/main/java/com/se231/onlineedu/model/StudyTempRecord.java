package com.se231.onlineedu.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author Zhe Li
 * @date 2019/09/06
 */
@Entity
public class StudyTempRecord {
    @EmbeddedId
    private LearnPrimaryKey learnPrimaryKey;

    @OneToOne
    @PrimaryKeyJoinColumn
    private Learn learn;

    private VideoAction prevState;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date prevTime;

    public StudyTempRecord(User user, Course course) {
        this.learnPrimaryKey = new LearnPrimaryKey(user,course);
    }

    public LearnPrimaryKey getLearnPrimaryKey() {
        return learnPrimaryKey;
    }

    public void setLearnPrimaryKey(LearnPrimaryKey learnPrimaryKey) {
        this.learnPrimaryKey = learnPrimaryKey;
    }

    public StudyTempRecord() {
    }

    public VideoAction getPrevState() {
        return prevState;
    }

    public void setPrevState(VideoAction prevState) {
        this.prevState = prevState;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getPrevTime() {
        return prevTime;
    }

    public void setPrevTime(Date prevTime) {
        this.prevTime = prevTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudyTempRecord that = (StudyTempRecord) o;
        return Objects.equals(learnPrimaryKey, that.learnPrimaryKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(learnPrimaryKey);
    }
}
