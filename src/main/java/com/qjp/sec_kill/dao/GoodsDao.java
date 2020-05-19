package com.qjp.sec_kill.dao;

import com.qjp.sec_kill.vo.goodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * description: GoodsDao
 * date: 2020/5/19 22:21
 * author: 雨夜微凉
 * version: 1.0
 */
@Mapper
public interface GoodsDao {

    @Select("select g.* ,mg.stock_count, mg.start_date, mg.end_date,mg.seckill_price  from miaosha_goods mg left join goods g on mg.goods_id=g.id")
    public List<goodsVo> listGoodsVo();


}
