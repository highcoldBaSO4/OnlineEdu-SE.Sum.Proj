package com.se231.onlineedu.scheduler;

import javax.annotation.PostConstruct;
import com.se231.onlineedu.service.CourseService;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Zhe Li
 * @date 2019/07/17
 */
@Component
public class SetCourseState implements Job {
    @Autowired
    private CourseService courseService;

    public static SetCourseState setCourseState;

    @PostConstruct
    public void init(){
        setCourseState =this;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDetail detail = context.getJobDetail();
        String state = detail.getJobDataMap().getString("state");
        Long courseId = detail.getJobDataMap().getLongValue("courseId");
        setCourseState.courseService.setState(courseId,state);
    }
}
