package com.qjp.sec_kill.redis;

public class GoodsKey extends BasePrefix{

	private GoodsKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
	public static GoodsKey getGoodsList = new GoodsKey(60, "gl");//页面缓存，访问统一页面
	public static GoodsKey getGoodsDetail = new GoodsKey(60, "gd");//url缓存，访问不同的页面，粒度稍微小
}
