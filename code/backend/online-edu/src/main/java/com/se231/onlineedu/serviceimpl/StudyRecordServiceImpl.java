package com.se231.onlineedu.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import com.se231.onlineedu.exception.NotFoundException;
import com.se231.onlineedu.message.request.TempRecord;
import com.se231.onlineedu.message.response.ReportAndTime;
import com.se231.onlineedu.message.response.StudyTime;
import com.se231.onlineedu.model.*;
import com.se231.onlineedu.repository.StudyRecordRepository;
import com.se231.onlineedu.repository.StudyReportRepository;
import com.se231.onlineedu.repository.StudyTempRecordRepository;
import com.se231.onlineedu.service.CourseService;
import com.se231.onlineedu.service.StudyRecordService;
import com.se231.onlineedu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Zhe Li
 * @date 2019/09/06
 */
@Service
public class StudyRecordServiceImpl implements StudyRecordService {
    @Autowired
    StudyTempRecordRepository studyTempRecordRepository;

    @Autowired
    StudyRecordRepository studyRecordRepository;

    @Autowired
    StudyReportRepository studyReportRepository;

    @Autowired
    UserService userService;

    @Autowired
    CourseService courseService;

    static final int MAX_STUDY_TIME = 720;

    @Override
    public StudyTempRecord submitRecord(Long courseId, Long userId, TempRecord tempRecord) {
        Course course = courseService.getCourseInfo(courseId);
        User user = userService.getUserInfo(userId);
        StudyTempRecord studyTempRecord = studyTempRecordRepository.findById(new LearnPrimaryKey(user,course))
                .orElse(new StudyTempRecord(user,course))   ;
        VideoAction videoAction = VideoAction.valueOf(tempRecord.getAction());
        java.sql.Date date = new java.sql.Date(tempRecord.getTime().getTime());
        StudyRecord studyRecord = studyRecordRepository.findById(new StudyRecordPrimaryKey(user,course,date))
                .orElse(new StudyRecord(user,course,date));
        switch (videoAction) {
            case CHANGE_SPEED:
                studyRecord.setChangeSpeedTime(studyRecord.getChangeSpeedTime() + 1);
                studyRecordRepository.save(studyRecord);
                return studyTempRecord;

            case JUMP :
                studyRecord.setJumpTimes(studyRecord.getJumpTimes() + 1);
                studyRecordRepository.save(studyRecord);
                return studyTempRecord;

            case START_PLAY:
                studyTempRecord.setPrevState(VideoAction.START_PLAY);
                studyTempRecord.setStartTime(tempRecord.getTime());
                return studyTempRecordRepository.save(studyTempRecord);

            case PAUSE:
                if(studyTempRecord.getPrevState()!=null&&studyTempRecord.getPrevState().equals(VideoAction.PAUSE)){
                    return studyTempRecord;
                }
                studyTempRecord.setPrevState(VideoAction.PAUSE);
                studyTempRecord.setPrevTime(tempRecord.getTime());
                return studyTempRecordRepository.save(studyTempRecord);

            default:
                if(studyTempRecord.getPrevState()==null){
                    studyTempRecord.setPrevState(videoAction);
                    System.out.println(studyTempRecord.getPrevState());
                    return studyTempRecordRepository.save(studyTempRecord);
                }
                if (studyTempRecord.getPrevState().equals(VideoAction.PAUSE)) {
                    Long pauseTime = tempRecord.getTime().getTime() - studyTempRecord.getPrevTime().getTime();
                    int pause = pauseTime.intValue()/60000;
                    studyRecord.setPauseTime((studyRecord.getPauseTime() + pause));
                    if(videoAction.equals(VideoAction.FINISH_WATCH)){
                        studyRecord.setTimeInMinute(((int)(tempRecord.getTime().getTime()-studyTempRecord.getStartTime().getTime())+studyRecord.getTimeInMinute())/60000);
                    }
                }

                else if(videoAction.equals(VideoAction.FINISH_WATCH)){
                    studyRecord.setTimeInMinute(((int)(tempRecord.getTime().getTime()-studyTempRecord.getStartTime().getTime())+studyRecord.getTimeInMinute())/60000);
                }
                if(studyRecord.getTimeInMinute()>MAX_STUDY_TIME){
                    studyRecord.setTimeInMinute(MAX_STUDY_TIME);
                }
                studyRecordRepository.save(studyRecord);
                studyTempRecord.setPrevState(videoAction);
                return studyTempRecordRepository.save(studyTempRecord);
            }
        }

    @Override
    public ReportAndTime getReport(Long courseId, Long userId) {
        User user = userService.getUserInfo(userId);
        Course course = courseService.getCourseInfo(courseId);
        ReportAndTime reportAndTime = new ReportAndTime();
        StudyReport studyReport = studyReportRepository.findById(new LearnPrimaryKey(user,course))
                .orElse(new StudyReport());
        reportAndTime.setReport(studyReport);
        List<StudyRecord> studyRecords = studyRecordRepository.findAllByStudyRecordPrimaryKey_UserAndStudyRecordPrimaryKey_Course(user,course);
        List<StudyTime> studyTimes = new ArrayList<>();
        studyRecords.forEach(studyRecord -> studyTimes.add(new StudyTime(studyRecord)));
        reportAndTime.setStudyTimes(studyTimes);
        return reportAndTime;
    }
}
