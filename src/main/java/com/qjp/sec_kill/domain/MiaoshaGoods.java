package com.qjp.sec_kill.domain;

import java.util.Date;

public class MiaoshaGoods {
	private Long id;
	private Long seckill_price;
	private Long goodsId;

	public Long getSeckill_price() {
		return seckill_price;
	}

	public void setSeckill_price(Long seckill_price) {
		this.seckill_price = seckill_price;
	}

	private Integer stockCount;
	private Date startDate;
	private Date endDate;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
	public Integer getStockCount() {
		return stockCount;
	}
	public void setStockCount(Integer stockCount) {
		this.stockCount = stockCount;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
