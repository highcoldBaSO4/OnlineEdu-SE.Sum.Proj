package com.se231.onlineedu.message.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Time Slot request form
 *
 * this form receive the request of setting course time.`
 *
 * @author Zhe Li
 * @date 2019/07/12
 */
@ApiModel("填写课程时间的表单")
public class TimeSlotForm {

    @ApiModelProperty(value = "时间段在周几",allowableValues = "0-6依次代表SUNDAY-SATURDAY")
    @NotNull
    private Integer day;

    @ApiModelProperty("开始时间是几点")
    @NotBlank
    private String start;

    @ApiModelProperty("结束时间是几点")
    @NotBlank
    private String end;

    public TimeSlotForm() {
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
