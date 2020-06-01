package com.qjp.sec_kill.vo;


import com.qjp.sec_kill.domain.OrderInfo;

public class OrderDetailVo {
	private goodsVo goods;
	private OrderInfo order;
	public goodsVo getGoods() {
		return goods;
	}
	public void setGoods(goodsVo goods) {
		this.goods = goods;
	}
	public OrderInfo getOrder() {
		return order;
	}
	public void setOrder(OrderInfo order) {
		this.order = order;
	}
}
