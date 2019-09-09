package com.se231.onlineedu.scheduler;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;
import java.util.Date;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

/**
 * 用于处理所有定时任务的封装类
 * @author lz
 * @date 2019/7/19
 */
public class SchedulerHandler {
    private static int courseTimes=0;
    private static int paperTimes=0;

    public static void setCourseState(Long courseId, String state, Date triggerDate)throws Exception {
        //创建调度器
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        //定义一个触发器
        Trigger trigger = newTrigger().withIdentity("trigger"+courseTimes, "course")
                .startNow()
                .startAt(triggerDate)
                .build();

        //定义一个JobDetail
        JobDetail job = newJob(SetCourseState.class)
                .withIdentity("course"+(courseTimes++), "course")
                .usingJobData("courseId", courseId)
                .usingJobData("state",state)
                .build();

        //调度加入这个job
        scheduler.scheduleJob(job, trigger);

        //启动
        scheduler.start();
    }

    public static void setAnswerStateAndAutoMark(Date triggerDate,Long paperId)throws Exception{
        //创建调度器
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        //定义一个触发器
        Trigger trigger = newTrigger().withIdentity("paperTrigger"+paperTimes, "paper")
                .startNow()
                .startAt(triggerDate)
                .build();

        //定义一个JobDetail
        JobDetail job = newJob(AnswerAutoMark.class)
                .withIdentity("paper"+(paperTimes++), "paper")
                .usingJobData("paperId", paperId)
                .build();

        //调度加入这个job
        scheduler.scheduleJob(job, trigger);

        //启动
        scheduler.start();
    }
}
