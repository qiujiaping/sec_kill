package com.qjp.sec_kill.domain;

public class MiaoshaOrder {
	private Long id;
	private Long userId;
	private Long  orderId;//通过订单id找到订单信息实体，订单包含货物id，用户id，下单时间，地址，秒杀价格etc
	private Long goodsId;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
}
