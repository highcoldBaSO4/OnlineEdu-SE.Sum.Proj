package com.se231.onlineedu.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Paper with questions entity class
 *
 * the weak entity used to record the questions in paper
 *
 * @author Zhe Li
 *
 * @date 2019/7/5
 */
@ApiModel("试卷中的一道题目,为弱实体集")
@Entity
@Table(name = "paper_with_questions")
public class PaperWithQuestions {

    @ApiModelProperty(value = "联合主键,由paper和question组成")
    @EmbeddedId
    private PaperWithQuestionsPrimaryKey paperWithQuestionsPrimaryKey;

    @ApiModelProperty("题号")
    private int questionNumber;

    @ApiModelProperty("分值")
    private double score;

    public PaperWithQuestions(Paper paper, Question question, int questionNumber, double score) {
        this.paperWithQuestionsPrimaryKey = new PaperWithQuestionsPrimaryKey(paper,question);
        this.questionNumber = questionNumber;
        this.score = score;
    }

    public PaperWithQuestions() {
    }

    public PaperWithQuestionsPrimaryKey getPaperWithQuestionsPrimaryKey() {
        return paperWithQuestionsPrimaryKey;
    }

    public void setPaperAnswerPrimaryKey(PaperWithQuestionsPrimaryKey paperWithQuestionsPrimaryKey) {
        this.paperWithQuestionsPrimaryKey = paperWithQuestionsPrimaryKey;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public double getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaperWithQuestions that = (PaperWithQuestions) o;
        return Objects.equals(paperWithQuestionsPrimaryKey, that.paperWithQuestionsPrimaryKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paperWithQuestionsPrimaryKey);
    }
}
