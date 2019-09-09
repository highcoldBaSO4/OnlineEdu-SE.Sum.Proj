package com.se231.onlineedu.message.request;

import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Paper's Question Form Class
 *
 * this class is used to generate a entire paper form
 *
 * @author Zhe Li
 *
 * @date 2019/7/5
 */
@ApiModel("试卷的小题的信息")
public class PaperQuestionForm {

    @NotNull
    @ApiModelProperty("题号")
    private int questionNumber;

    @ApiModelProperty("题目的id")
    @NotNull
    private Long questionId;

    @ApiModelProperty("分值")
    private double score;

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
