package com.qjp.sec_kill.rabbitmq;

import com.qjp.sec_kill.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * description: MQsenders
 * date: 2020/6/1 23:23
 * author: 雨夜微凉
 * version: 1.0
 */
@Service
public class MQsenders {
    @Autowired
    AmqpTemplate amqpTemplate ;
    public Logger log= LoggerFactory.getLogger(MQsenders.class);

    public void send(Object mm) {
        String msg = RedisService.beanToString(mm);
        log.info("send message:"+msg);
        amqpTemplate.convertAndSend(MQConfig.QUEUE, msg);
    }

    public void sendmiaoshaMessage(MiaoshaMessage miaoshaMessage) {
        String msg = RedisService.beanToString(miaoshaMessage);
        log.info("send message:"+msg);
        amqpTemplate.convertAndSend(MQConfig.MIAOSHA_QUEUE, msg);
    }
}
