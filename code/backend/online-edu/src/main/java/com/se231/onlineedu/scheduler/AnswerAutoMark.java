package com.se231.onlineedu.scheduler;

import javax.annotation.PostConstruct;
import com.se231.onlineedu.service.PaperAnswerService;
import com.se231.onlineedu.service.PaperService;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Zhe Li
 * @date 2019/07/19
 */
@Component
public class AnswerAutoMark implements Job {

    @Autowired
    PaperAnswerService paperAnswerService;

    private static AnswerAutoMark answerAutoMark;

    @PostConstruct
    public void init(){
        answerAutoMark=this;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDetail detail = jobExecutionContext.getJobDetail();
        answerAutoMark.paperAnswerService.autoMark(detail.getJobDataMap().getLongValue("paperId"));
    }
}
