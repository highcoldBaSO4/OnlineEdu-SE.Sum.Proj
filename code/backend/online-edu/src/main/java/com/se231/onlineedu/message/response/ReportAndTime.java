package com.se231.onlineedu.message.response;

import java.util.List;
import com.se231.onlineedu.model.StudyReport;

/**
 * @author Zhe Li
 * @date 2019/09/08
 */
public class ReportAndTime {
    StudyReport report;

    List<StudyTime> studyTimes;

    public ReportAndTime() {
    }

    public StudyReport getReport() {
        return report;
    }

    public void setReport(StudyReport report) {
        this.report = report;
    }

    public List<StudyTime> getStudyTimes() {
        return studyTimes;
    }

    public void setStudyTimes(List<StudyTime> studyTimes) {
        this.studyTimes = studyTimes;
    }
}
