package com.qjp.sec_kill.service;

import com.qjp.sec_kill.domain.MiaoshaOrder;
import com.qjp.sec_kill.domain.MiaoshaUser;
import com.qjp.sec_kill.domain.OrderInfo;
import com.qjp.sec_kill.redis.MiaoshaKey;
import com.qjp.sec_kill.redis.RedisService;
import com.qjp.sec_kill.vo.goodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * description: MiaoshaService
 * date: 2020/5/24 23:55
 * author: 雨夜微凉
 * version: 1.0
 */
@Service
public class MiaoshaService {

    //为了在自己模块不引入命名不一致的dao，需要引入其他的service
    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;
    @Autowired
    RedisService redisService;


    @Transactional
    public OrderInfo miaosha(MiaoshaUser miaoshaUser, goodsVo goodsVo){
        //减库存 下订单(写入秒杀订单),返回订单信息
        boolean success = goodsService.reduceStock(goodsVo);
        //如果减库存成功则产生订单
        if(success){
            return  orderService.createOrder(miaoshaUser, goodsVo);
        }
        //否则秒杀失败（库存不足）
        else {
            setGoodsOver(goodsVo.getId());
            return null;
        }

    }

    public long getMIAOSHARESULT(Long useId, Long goodId) {
        MiaoshaOrder miaoshaOrder = orderService.getMiaoshaOrderByUserIdGoodsId(useId, goodId);
        //秒杀成功
        if(miaoshaOrder!=null){
            return miaoshaOrder.getOrderId();
        }
        //这里要区分是还在排队还是秒杀失败（库存不足）
        else {
            boolean isover=getGoodsOver(goodId);
            if(isover){
                return -1;
            }
            else return 0;
        }
    }

    private boolean getGoodsOver(Long goodId) {
        boolean exists = redisService.exists(MiaoshaKey.isGoodsOver, "" + goodId);
        return exists;
    }

    public void setGoodsOver(Long goodId){
        redisService.set(MiaoshaKey.isGoodsOver,""+goodId,true);
    }
}
