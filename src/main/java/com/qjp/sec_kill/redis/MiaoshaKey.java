package com.qjp.sec_kill.redis;

public class MiaoshaKey extends BasePrefix{

	private MiaoshaKey(int expireSeconds,String prefix) {
		super(expireSeconds,prefix);
	}
	public static MiaoshaKey isGoodsOver = new MiaoshaKey(0,"go");
	public static MiaoshaKey getMiaoshaPath=new MiaoshaKey(60, "mp");
}
