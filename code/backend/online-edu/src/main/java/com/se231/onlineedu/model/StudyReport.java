package com.se231.onlineedu.model;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author Zhe Li
 * @date 2019/09/07
 */
@Entity
public class StudyReport {
    @EmbeddedId
    private LearnPrimaryKey learnPrimaryKey;

    @OneToOne
    @PrimaryKeyJoinColumn
    private Learn learn;

    private int hardworking;

    private int concentration;

    private int studyTime;

    public LearnPrimaryKey getLearnPrimaryKey() {
        return learnPrimaryKey;
    }

    public void setLearnPrimaryKey(LearnPrimaryKey learnPrimaryKey) {
        this.learnPrimaryKey = learnPrimaryKey;
    }

    public Learn getLearn() {
        return learn;
    }

    public void setLearn(Learn learn) {
        this.learn = learn;
    }

    public int getHardworking() {
        return hardworking;
    }

    public void setHardworking(int hardworking) {
        this.hardworking = hardworking;
    }

    public int getConcentration() {
        return concentration;
    }

    public void setConcentration(int concentration) {
        this.concentration = concentration;
    }

    public int getStudyTime() {
        return studyTime;
    }

    public void setStudyTime(int studyTime) {
        this.studyTime = studyTime;
    }

    public StudyReport() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudyReport that = (StudyReport) o;
        return learnPrimaryKey == that.learnPrimaryKey;
    }

    @Override
    public int hashCode() {
        return Objects.hash(learnPrimaryKey);
    }
}
