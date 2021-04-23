package com.se231.onlineedu.message.request;

import javax.validation.constraints.NotNull;

/**
 * Question Answer Form
 *
 * this class is used to help student answer a single question.
 *
 * @author Zhe Li
 * @date 2019/07/15
 */
public class QuestionAnswer {
    @NotNull
    private Long questionId;

    private String answer;

    public QuestionAnswer(@NotNull Long questionId, String answer) {
        this.questionId = questionId;
        this.answer = answer;
    }

    public QuestionAnswer() {
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
