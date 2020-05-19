package com.qjp.sec_kill.vo;

import com.qjp.sec_kill.domain.Goods;

import java.util.Date;

/**
 * description: goodsVo
 * date: 2020/5/19 22:16
 * author: 雨夜微凉
 * version: 1.0
 */
public class goodsVo extends Goods {
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
    private Double seckillprice;

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

    public Double getSeckillprice() {
        return seckillprice;
    }

    public void setSeckillprice(Double seckillprice) {
        this.seckillprice = seckillprice;
    }

    @Override
    public String toString() {
        return "goodsVo{" +
                "stockCount=" + stockCount +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", seckill_price=" + seckillprice +
                '}';
    }
}
