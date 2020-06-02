package com.qjp.sec_kill.controller;

import com.qjp.sec_kill.domain.MiaoshaOrder;
import com.qjp.sec_kill.domain.MiaoshaUser;

import com.qjp.sec_kill.rabbitmq.MQsenders;
import com.qjp.sec_kill.rabbitmq.MiaoshaMessage;
import com.qjp.sec_kill.redis.GoodsKey;
import com.qjp.sec_kill.redis.RedisService;
import com.qjp.sec_kill.result.CodeMsg;
import com.qjp.sec_kill.result.Result;
import com.qjp.sec_kill.service.GoodsService;
import com.qjp.sec_kill.service.MiaoshaService;
import com.qjp.sec_kill.service.OrderService;
import com.qjp.sec_kill.vo.goodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

/**
 * description: miaoshaController
 * date: 2020/5/20 0:44
 * author: 雨夜微凉
 * version: 1.0
 */
@Controller
@RequestMapping("/miaosha")
public class miaoshaController implements InitializingBean {
    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;
    @Autowired
    MiaoshaService miaoshaService;
    @Autowired
    RedisService redisService;
    @Autowired
    MQsenders mQsenders;
    private HashMap<Long, Boolean> localOverMap =  new HashMap<Long, Boolean>();
    @Override
    public void afterPropertiesSet() throws Exception {
        List<goodsVo> goodsVos = goodsService.listGoodsVo();
        for (goodsVo good:goodsVos) {
            redisService.set(GoodsKey.getMiaoshaGoodsStock, ""+good.getId(), good.getStockCount());
            localOverMap.put(good.getId(), false);
        }
    }
    @RequestMapping("do_miaosha")
    @ResponseBody
    public Result<Integer> domiaosha(Model model, MiaoshaUser miaoshaUser, @RequestParam("goodsId") Long id){
        if(miaoshaUser==null){//如果用户未登录则到登录页面进行登录
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        /*1：请求过来需要预减库存*/
        Long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock, "" + id);
        if(stock<0){//需要特别注意这里是0
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        //2：判断是否秒杀到，不能重复秒杀
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(miaoshaUser.getId(), id);
        if(order != null) {
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }
        //3：入队
        MiaoshaMessage miaoshaMessage = new MiaoshaMessage();
        miaoshaMessage.setUser(miaoshaUser);
        miaoshaMessage.setGoodsId(id);
        mQsenders.sendmiaoshaMessage(miaoshaMessage);
        return Result.success(0);
    }

    /**
     *客户端轮询的接口
     * @return
     * orderid:成功
     * -1：库存不足
     * 0：还在排队
     */
    @RequestMapping(value = "/result",method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> MIAOSHARESULT(Model model, MiaoshaUser miaoshaUser, @RequestParam("goodsId") Long id){
        if(miaoshaUser==null){//如果用户未登录则到登录页面进行登录
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long result=miaoshaService.getMIAOSHARESULT(miaoshaUser.getId(),id);
        return Result.success(result);
    }
//    @RequestMapping("do_miaosha")
//    @ResponseBody
//    public Result<OrderInfo> domiaosha(Model model, MiaoshaUser miaoshaUser, @RequestParam("goodsId") Long id){
//
//        if(miaoshaUser==null){//如果用户未登录则到登录页面进行登录
//            return Result.error(CodeMsg.SESSION_ERROR);
//        }
//
//        goodsVo goods = goodsService.getGoodsVoById(id);//进行秒杀的商品
//        Integer stockCount = goods.getStockCount();
//        //1，判断库存
//        if(stockCount <= 0){
//            return Result.error(CodeMsg.MIAO_SHA_OVER);
//        }
//       //2，判断是否秒杀到，不能重复秒杀
//        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(miaoshaUser.getId(), id);
//        if(order != null) {
//            return Result.error(CodeMsg.REPEATE_MIAOSHA);
//        }
//
//        //3，减少库存，下订单，写入秒杀订单
//        OrderInfo orderInfo = miaoshaService.miaosha(miaoshaUser, goods);
//
//        return Result.success(orderInfo);
//    }

    
}
