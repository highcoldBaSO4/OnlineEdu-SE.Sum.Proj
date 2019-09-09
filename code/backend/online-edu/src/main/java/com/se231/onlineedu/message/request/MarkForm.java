package com.se231.onlineedu.message.request;

import java.util.Objects;

/**
 * Mark Form Class
 *
 * this class is used to help teacher mark paper
 *
 * @author Zhe Li
 * @date 2019/07/24
 */
public class MarkForm {
    private String comment;

    private Long questionId;

    private Double score;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public MarkForm() {
    }

    public MarkForm(String comment, Long questionId, Double score) {
        this.comment = comment;
        this.questionId = questionId;
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MarkForm markForm = (MarkForm) o;
        return questionId.equals(markForm.questionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionId);
    }
}
