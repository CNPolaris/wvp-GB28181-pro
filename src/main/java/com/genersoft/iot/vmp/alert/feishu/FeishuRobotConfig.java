package com.genersoft.iot.vmp.alert.feishu;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class FeishuRobotConfig {

    @Resource
    private FeishuProperties feishuProperties;

    /**
     * 飞书专用RestTemplate
     */
    @Bean
    public RestTemplate feishuRestTemplate() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(feishuProperties.getRobot().getConnectTimeout());
        requestFactory.setReadTimeout(feishuProperties.getRobot().getReadTimeout());

        return new RestTemplate(requestFactory);
    }

    /**
     * 异步发送消息的线程池
     */
    @Bean(name = "feishuAsyncExecutor")
    public Executor feishuAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程数
        executor.setCorePoolSize(3);
        // 最大线程数
        executor.setMaxPoolSize(5);
        // 队列容量
        executor.setQueueCapacity(100);
        // 线程名称前缀
        executor.setThreadNamePrefix("FeishuAsync-");
        // 当线程池达到最大线程数时的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 初始化
        executor.initialize();
        return executor;
    }
}
