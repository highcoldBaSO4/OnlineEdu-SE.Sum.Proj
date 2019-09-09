package com.se231.onlineedu.message.request;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Submit Answer Form Class
 *
 * this form is used to help student submit their answer to a paper.
 *
 * @author Zhe Li
 * @date 2019/07/15
 */
public class SubmitAnswerForm {
    private List<QuestionAnswer> answerList;

    @NotNull
    private String state;

    public SubmitAnswerForm(List<QuestionAnswer> answerList, String state) {
        this.answerList = answerList;
        this.state = state;
    }

    public SubmitAnswerForm() {
    }

    public List<QuestionAnswer> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<QuestionAnswer> answerList) {
        this.answerList = answerList;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
