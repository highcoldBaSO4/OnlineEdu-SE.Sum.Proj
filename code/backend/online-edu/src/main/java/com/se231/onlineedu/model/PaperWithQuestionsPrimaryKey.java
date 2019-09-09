package com.se231.onlineedu.model;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Paper and questions entity primary key
 *
 * the embedded primary key class of questions and questions
 *
 * @author Zhe Li
 *
 * @date 2019/7/5
 */
@Embeddable
public class PaperWithQuestionsPrimaryKey implements Serializable {
    private static final Long serialVersionUID = 1L;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "paper_id")
    @JsonBackReference
    private Paper paper;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id")
    private Question question;

    public PaperWithQuestionsPrimaryKey() {
    }

    public PaperWithQuestionsPrimaryKey(Paper paper, Question question) {
        this.paper = paper;
        this.question = question;
    }

    public Paper getPaper() {
        return paper;
    }

    public void setPaper(Paper paper) {
        this.paper = paper;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaperWithQuestionsPrimaryKey that = (PaperWithQuestionsPrimaryKey) o;
        return paper.equals(that.paper) &&
                question.equals(that.question);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paper, question);
    }
}
