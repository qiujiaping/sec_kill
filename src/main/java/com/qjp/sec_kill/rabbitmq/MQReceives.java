package com.qjp.sec_kill.rabbitmq;

import com.qjp.sec_kill.domain.MiaoshaOrder;
import com.qjp.sec_kill.domain.MiaoshaUser;
import com.qjp.sec_kill.domain.OrderInfo;
import com.qjp.sec_kill.redis.RedisService;
import com.qjp.sec_kill.service.GoodsService;
import com.qjp.sec_kill.service.MiaoshaService;
import com.qjp.sec_kill.service.OrderService;
import com.qjp.sec_kill.vo.goodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * description: MQReceives
 * date: 2020/6/1 23:24
 * author: 雨夜微凉
 * version: 1.0
 */
@Service
public class MQReceives {
    public Logger log= LoggerFactory.getLogger(MQsenders.class);
    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;
    @Autowired
    MiaoshaService miaoshaService;


    @RabbitListener(queues=MQConfig.MIAOSHA_QUEUE)
    public void receive(String message) {
        log.info("receive message:" + message);
        MiaoshaMessage miaoshaMessage = RedisService.stringToBean(message, MiaoshaMessage.class);
        MiaoshaUser user = miaoshaMessage.getUser();
        long goodsId = miaoshaMessage.getGoodsId();
        goodsVo goods = goodsService.getGoodsVoById(goodsId);
        int stockCount = goods.getStockCount();
        if(stockCount<=0){
            return;
        }
        //判断是否秒杀到（其实可以不判断）
        MiaoshaOrder isExist = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if(isExist!=null){return;}

        //真正的秒杀
        //3，减少库存，下订单，写入秒杀订单
        OrderInfo orderInfo = miaoshaService.miaosha(user, goods);

    }
    //    @RabbitListener(queues=MQConfig.QUEUE)
//    public void receive(String message) {
//        log.info("receive message:"+message);
//        MiaoshaMessage mm  = RedisService.stringToBean(message, MiaoshaMessage.class);
//        MiaoshaUser user = mm.getUser();
//        long goodsId = mm.getGoodsId();
//
//        goodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
//        int stock = goods.getStockCount();
//        if(stock <= 0) {
//            return;
//        }
//        //判断是否已经秒杀到了
//        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
//        if(order != null) {
//            return;
//        }
//        //减库存 下订单 写入秒杀订单
//        miaoshaService.miaosha(user, goods);
//    }
}
