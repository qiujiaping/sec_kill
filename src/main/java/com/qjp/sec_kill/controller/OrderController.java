package com.qjp.sec_kill.controller;

import com.qjp.sec_kill.domain.MiaoshaUser;
import com.qjp.sec_kill.domain.OrderInfo;
import com.qjp.sec_kill.redis.RedisService;
import com.qjp.sec_kill.result.CodeMsg;
import com.qjp.sec_kill.result.Result;
import com.qjp.sec_kill.service.GoodsService;
import com.qjp.sec_kill.service.MiaoshaUserService;
import com.qjp.sec_kill.service.OrderService;
import com.qjp.sec_kill.vo.OrderDetailVo;
import com.qjp.sec_kill.vo.goodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/order")
public class OrderController {

	@Autowired
	MiaoshaUserService userService;
	
	@Autowired
	RedisService redisService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	GoodsService goodsService;
	
    @RequestMapping("/detail")
    @ResponseBody
    public Result<OrderDetailVo> info(Model model, MiaoshaUser user,
									  @RequestParam("orderId") long orderId) {
    	if(user == null) {
    		return Result.error(CodeMsg.SESSION_ERROR);
    	}
    	OrderInfo order = orderService.getOrderById(orderId);
    	if(order == null) {
    		return Result.error(CodeMsg.ORDER_NOT_EXIST);
    	}
    	long goodsId = order.getGoodsId();
    	goodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
    	OrderDetailVo vo = new OrderDetailVo();
    	vo.setOrder(order);
    	vo.setGoods(goods);
    	return Result.success(vo);
    }
    
}
