package com.se231.onlineedu.scheduler;

import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

/**
 * @author Zhe Li
 * @date 2019/07/17
 */
@Configuration
public class SchedulerConfig implements SchedulingConfigurer {
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());
    }

    @Bean()
    public Executor taskExecutor() {
        return new ScheduledThreadPoolExecutor
                (10,new BasicThreadFactory.Builder().namingPattern("task-pool-%d").daemon(true).build());
    }

}
