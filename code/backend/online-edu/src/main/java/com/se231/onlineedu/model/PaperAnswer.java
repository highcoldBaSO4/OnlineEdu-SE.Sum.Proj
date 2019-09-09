package com.se231.onlineedu.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Paper Answer Entity Class
 *
 * answer for a paper submitted by a user
 *
 * @author Yuxuan Liu
 *
 * @date 2019/7/4
 */
@ApiModel("学生做一套试卷的答案")
@Entity
public class PaperAnswer {
    @ApiModelProperty("联合主键,由学生,paper,次数组成,目前限答三次")
    @EmbeddedId
    private PaperAnswerPrimaryKey paperAnswerPrimaryKey;

    @ApiModelProperty("学生对每道题的答案的集,详情请见answer")
    @OneToMany(mappedBy = "answerPrimaryKey.paperAnswer",cascade = CascadeType.ALL)
    private List<Answer> answers;

    @ApiModelProperty("学生此次答题的成绩")
    private double grade;

    @ApiModelProperty(value = "学生这次作业的状态",example = "NOT_START:未开始,NOT_FINISH:开始为完成(暂存)," +
            "FINISHED:已完成并提交,但未到批改阶段,NOT_MARKED:已到批改阶段但仍未被批改,MARKED:已被批改,有成绩")
    @Enumerated(value = EnumType.STRING)
    private PaperAnswerState state;

    public PaperAnswer(PaperAnswerPrimaryKey paperAnswerPrimaryKey, List<Answer> answers, double grade) {
        this.paperAnswerPrimaryKey = paperAnswerPrimaryKey;
        this.answers = answers;
        this.grade = grade;
    }

    public PaperAnswer(PaperAnswerPrimaryKey paperAnswerPrimaryKey) {
        this.paperAnswerPrimaryKey = paperAnswerPrimaryKey;
        answers = new ArrayList<>();
    }

    public PaperAnswerPrimaryKey getPaperAnswerPrimaryKey() {
        return paperAnswerPrimaryKey;
    }

    public void setPaperAnswerPrimaryKey(PaperAnswerPrimaryKey paperAnswerPrimaryKey) {
        this.paperAnswerPrimaryKey = paperAnswerPrimaryKey;
    }

    public PaperAnswer() {
        answers = new ArrayList<>();
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public PaperAnswerState getState() {
        return state;
    }

    public void setState(PaperAnswerState state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaperAnswer that = (PaperAnswer) o;
        return paperAnswerPrimaryKey.equals(that.paperAnswerPrimaryKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paperAnswerPrimaryKey);
    }
}
