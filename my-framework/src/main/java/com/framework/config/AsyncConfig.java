package com.framework.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;

@Configuration
public class AsyncConfig {

    private Logger log = LoggerFactory.getLogger(AsyncConfig.class);

    private static final int CORE_POOL_SIZE = 2;

    private static final int MAXIMUM_POOL_SIZE = 5;

    private static final long KEEP_ALIVE_TIME = 60;

    private static final TimeUnit UNIT = TimeUnit.SECONDS;

    private static final LinkedBlockingQueue LINKED_BLOCKING_QUEUE = new LinkedBlockingQueue(50);

    //自定义Spring默认线程池
    //ThreadPoolTaskExecutor vs ThreadPoolExecutor 区别在于：
    //ThreadPoolTaskExecutor是对ThreadPoolExecutor的进一步封装
    //ThreadPoolTaskExecutor来源于Spring，ThreadPoolExecutor属于JUC
    //ThreadPoolTaskExecutor需要声明initialize，ThreadPoolExecutor不需要
    @Bean("common")
    public Executor commonExecutorBuild() {
        log.info("Common Executor Building Start!");

        //ThreadPoolExecutor executor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME,
        //                                                     UNIT, LINKED_BLOCKING_QUEUE, new ThreadPoolExecutor.CallerRunsPolicy());

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(2);
        //配置最大线程数
        executor.setMaxPoolSize(5);
        //配置队列大小
        executor.setQueueCapacity(10240);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("AsyncCommonThread-");
        // 设置拒绝策略：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();

        return executor;
    }

}
