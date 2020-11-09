package com.blog.system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author GGXian
 * @project MiniBlog
 * @createTime 2020/10/26 15:41
 * @description 多线程配置类
 **/
@EnableAsync
@Configuration
public class AsyncConfig {
    /**
     * 当前主机的处理器核心数
     */
    private static final int PROCESSORS = Runtime.getRuntime().availableProcessors();
    /**
     * 最大线程数=处理器核心数+3
     */
    private static final int MAXINUM = PROCESSORS + 3;

    @Bean("taskExecutor")
    public AsyncTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setMaxPoolSize(MAXINUM);
        taskExecutor.setCorePoolSize(PROCESSORS);
        taskExecutor.setThreadNamePrefix("async-task-thread-pool");
        taskExecutor.initialize();
        return taskExecutor;
    }
}
