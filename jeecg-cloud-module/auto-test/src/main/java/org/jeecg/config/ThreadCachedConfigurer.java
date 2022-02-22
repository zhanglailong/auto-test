package org.jeecg.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author zlf
 * @version V1.0
 * @date 2021/1/15
 */
@Component
public class ThreadCachedConfigurer implements AsyncConfigurer {

    @Bean
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();
        //设置核心线程数 方法: 返回可用处理器的Java虚拟机的数量。
        threadPool.setCorePoolSize(Runtime.getRuntime().availableProcessors());
        //设置最大线程数
        threadPool.setMaxPoolSize(Runtime.getRuntime().availableProcessors()*5);
        //线程池所使用的缓冲队列
        threadPool.setQueueCapacity(Runtime.getRuntime().availableProcessors()*2);
        //等待任务在关机时完成--表明等待所有线程执行完
        threadPool.setWaitForTasksToCompleteOnShutdown(true);
        // 等待时间 （默认为0，此时立即停止），并没等待xx秒后强制停止
        threadPool.setAwaitTerminationSeconds(60);
        //  线程名称前缀
        threadPool.setThreadNamePrefix("ServerAsync-");
        //拒绝策略 直接抛弃任务，不予任何处理也不抛出异常
        threadPool.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        // 初始化线程
        threadPool.initialize();
        return threadPool;
    }


    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }

}
