package com.qjp.sec_kill.service;

import com.qjp.sec_kill.domain.MiaoshaUser;
import com.qjp.sec_kill.domain.OrderInfo;
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


    @Transactional
    public OrderInfo miaosha(MiaoshaUser miaoshaUser, goodsVo goodsVo){
        //减库存 下订单(写入秒杀订单),返回订单信息
        goodsService.reduceStock(goodsVo);
        //order_info maiosha_order
        return orderService.createOrder(miaoshaUser, goodsVo);
    }
}
