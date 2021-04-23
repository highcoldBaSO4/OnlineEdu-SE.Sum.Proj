package com.se231.onlineedu.message.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *  Submit Question form bean
 *
 *  this bean is used to help teaching admin upload a new question.
 *
 * @author Zhe Li
 *
 * @date 2019/7/5
 */
@ApiModel("上传题目的表单")
public class SubmitQuestionForm {

    @ApiModelProperty(value = "题目的类型",required = true,example = "singe_answer,t_or_f,multiple_answer,subjective")
    @NotBlank
    private String questionType;

    @ApiModelProperty("问题描述,包含选项等")
    @Size(max = 1000)
    private String question;

    @ApiModelProperty("题目的参考答案")
    @Size(max = 1000)
    private String answer;

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
