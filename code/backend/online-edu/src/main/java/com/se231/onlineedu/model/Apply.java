package com.se231.onlineedu.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * Teacher Apply for Course Class
 *
 * This weak entity is used to record the application for a course prototype of a teacher.
 *
 * @author Zhe Li
 *
 * @date 2019/7/3
 */

@ApiModel("教师对课程原型的使用权的申请")
@Entity
@Table(name = "Teacher_Apply_For_Course")
@JsonIgnoreProperties("hibernateLazyInitializer")
public class Apply {

    @ApiModelProperty("联合主键类,组成为课程原型和教师")
    @EmbeddedId
    private ApplyPrimaryKey applicationForCoursePK;

    @ApiModelProperty("该申请的状态,目前有:DISAPPROVAL,NOT_DECIDE,APPROVAL")
    @NotNull
    @Enumerated(EnumType.STRING)
    private ApplyState applyState;

    public Apply() {
    }

    public Apply(ApplyPrimaryKey applicationForCoursePK) {
        this.applicationForCoursePK = applicationForCoursePK;
        this.applyState=ApplyState.NOT_DECIDE;
    }

    public ApplyPrimaryKey getApplicationForCoursePK() {
        return applicationForCoursePK;
    }

    public void setApplicationForCoursePK(ApplyPrimaryKey applicationForCoursePK) {
        this.applicationForCoursePK = applicationForCoursePK;
    }

    public ApplyState getState() {
        return applyState;
    }

    public void setApplyState(ApplyState state) {
        this.applyState = state;
    }
}
