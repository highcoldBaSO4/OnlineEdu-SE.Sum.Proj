package com.se231.onlineedu.model;


import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.util.Objects;

@Entity
public class Learn {
    @EmbeddedId
    private LearnPrimaryKey learnPrimaryKey;

    private double grade =-1;

    private int signInTimes = 0;

    private String wordCloudUrl;

    public Learn() {
    }

    public Learn(User user, Course course) {
        this.learnPrimaryKey = new LearnPrimaryKey(user, course);
    }

    public LearnPrimaryKey getLearnPrimaryKey() {
        return learnPrimaryKey;
    }

    public void setLearnPrimaryKey(LearnPrimaryKey learnPrimaryKey) {
        this.learnPrimaryKey = learnPrimaryKey;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public int getSignInTimes() {
        return signInTimes;
    }

    public void setSignInTimes(int signInTimes) {
        this.signInTimes = signInTimes;
    }

    public String getWordCloudUrl() {
        return wordCloudUrl;
    }


    public void setWordCloudUrl(String wordCloudUrl) {
        this.wordCloudUrl = wordCloudUrl;
    }

    public Learn(LearnPrimaryKey learnPrimaryKey, int grade, int signInTimes) {
        this.learnPrimaryKey = learnPrimaryKey;
        this.grade = grade;
        this.signInTimes = signInTimes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Learn learn = (Learn) o;
        return Objects.equals(learnPrimaryKey, learn.learnPrimaryKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(learnPrimaryKey);
    }
}
