package com.qjp.sec_kill.service;

import com.qjp.sec_kill.dao.OrderDao;
import com.qjp.sec_kill.domain.MiaoshaOrder;
import com.qjp.sec_kill.domain.MiaoshaUser;
import com.qjp.sec_kill.domain.OrderInfo;
import com.qjp.sec_kill.redis.OrderKey;
import com.qjp.sec_kill.redis.RedisService;
import com.qjp.sec_kill.vo.goodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * description: OrderService
 * date: 2020/5/24 23:39
 * author: 雨夜微凉
 * version: 1.0
 */
@Service
public class OrderService {

    @Autowired
    OrderDao orderDao;

    //查看订单是否秒杀到直接到缓存查不到数据库查（优化）
    @Autowired
    RedisService redisService;

    //查看订单是否秒杀到直接到缓存查不到数据库
    public  MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(Long userId, Long goodsId) {
        MiaoshaOrder miaoshaOrder= redisService.get(OrderKey.getMiaoshaOrderByUserIdGoodsId,""+userId+"_"+goodsId,MiaoshaOrder.class);

        return miaoshaOrder;
//        return orderDao.getMiaoshaOrderByUserIdGoodsId(userId, goodsId);
    }

    //在数据库生成订单信息并返回，在数据库生成秒杀订单
    @Transactional
    public OrderInfo createOrder(MiaoshaUser user, goodsVo goods) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getSeckillprice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        long orderId = orderDao.insertOrderInfo(orderInfo);
        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setGoodsId(goods.getId());
        miaoshaOrder.setOrderId(orderInfo.getId());
        miaoshaOrder.setUserId(user.getId());
        orderDao.insertMiaoshaOrder(miaoshaOrder);
        redisService.set(OrderKey.getMiaoshaOrderByUserIdGoodsId,""+user.getId()+"_"+goods.getId(),miaoshaOrder);

        return orderInfo;
    }

    public OrderInfo getOrderById(long orderId) {
        return  orderDao.getOrderById(orderId);
    }
}
