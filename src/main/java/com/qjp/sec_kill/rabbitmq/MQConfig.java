package com.qjp.sec_kill.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * description: MQConfig
 * date: 2020/6/1 23:25
 * author: 雨夜微凉
 * version: 1.0
 */


//定义queue，配置类
@Configuration
public class MQConfig {

    public static final String MIAOSHA_QUEUE = "miaosha.queue";
    public static final String QUEUE = "queue";
    /**
     * Direct模式 交换机Exchange
     * */
    @Bean
    public Queue queue() {
        return new Queue(QUEUE, true);
    }
    @Bean
    public Queue queue2() {
        return new Queue(MIAOSHA_QUEUE, true);
    }
}
