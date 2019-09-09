package com.se231.onlineedu.message.response;

import java.sql.Date;
import com.se231.onlineedu.model.StudyRecord;

/**
 * @author Zhe Li
 * @date 2019/09/08
 */
public class StudyTime {
    int minute;

    Date date;

    public StudyTime() {
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public StudyTime(StudyRecord studyRecord){
        this.date = studyRecord.getStudyRecordPrimaryKey().getStudyDate();
        this.minute = studyRecord.getTimeInMinute()-studyRecord.getPauseTime();
    }
}
