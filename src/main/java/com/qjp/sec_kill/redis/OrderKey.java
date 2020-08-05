package com.qjp.sec_kill.redis;

public class OrderKey extends BasePrefix {

	//不设置过期时间，永久有效
	public OrderKey( String prefix) {
		super(prefix);
	}

	public static OrderKey getMiaoshaOrderByUserIdGoodsId=new OrderKey("moug");

}
