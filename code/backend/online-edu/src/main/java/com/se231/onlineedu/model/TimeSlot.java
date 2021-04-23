package com.se231.onlineedu.model;

import javax.persistence.*;
import java.sql.Time;
import java.util.List;
import com.se231.onlineedu.message.request.TimeSlotForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author Zhe Li
 * @date 2019/07/12
 */
@Entity
@ApiModel
@Table(name = "time_slot")
public class TimeSlot {
    @ApiModelProperty("时间段id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty("开始时间")
    private Time start;

    @ApiModelProperty("结束时间")
    private Time end;

    @Enumerated
    @ApiModelProperty("上课日期在周几")
    private WeekDay day;

    @ManyToMany(mappedBy = "timeSlots")
    private List<Course> courseList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Time getStart() {
        return start;
    }

    public void setStart(Time start) {
        this.start = start;
    }

    public Time getEnd() {
        return end;
    }

    public void setEnd(Time end) {
        this.end = end;
    }

    public WeekDay getDay() {
        return day;
    }

    public void setDay(WeekDay day) {
        this.day = day;
    }

    public TimeSlot() {
    }

    public TimeSlot(TimeSlotForm form){
        start=Time.valueOf(form.getStart()+":00");
        end=Time.valueOf(form.getEnd()+":00");
        day=WeekDay.values()[form.getDay()];
    }
}
