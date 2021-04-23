package com.se231.onlineedu.model;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * Answer Primary Key Class
 *
 * an answer for a specific question must be identified by paper and question.
 *
 * @author Yuxuan Liu
 *
 * @date 2019/7/4
 */
@Embeddable
public class AnswerPrimaryKey implements Serializable {
    private static final Long serialVersionUID = 1L;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JsonBackReference
    private PaperAnswer paperAnswer;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    public AnswerPrimaryKey(PaperAnswer paperAnswer, Question question) {
        this.paperAnswer = paperAnswer;
        this.question = question;
    }

    public PaperAnswer getPaperAnswer() {
        return paperAnswer;
    }

    public void setPaperAnswer(PaperAnswer paperAnswer) {
        this.paperAnswer = paperAnswer;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public AnswerPrimaryKey() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnswerPrimaryKey that = (AnswerPrimaryKey) o;
        return Objects.equals(paperAnswer, that.paperAnswer) &&
                Objects.equals(question, that.question);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paperAnswer, question);
    }
}
