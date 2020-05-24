package com.qjp.sec_kill.service;

import com.qjp.sec_kill.dao.GoodsDao;
import com.qjp.sec_kill.domain.MiaoshaGoods;
import com.qjp.sec_kill.vo.goodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * description: MiaoshaUserService
 * date: 2020/5/18 20:39
 * author: 雨夜微凉
 * version: 1.0
 */
@Service
public class GoodsService{
    @Autowired
    GoodsDao goodsDao;
    public List<goodsVo>  listGoodsVo(){
        List<goodsVo> list=goodsDao.listGoodsVo();
        return list;
    }


    public goodsVo getGoodsVoById(Long id) {
        goodsVo goodsvo=goodsDao.getGoodsVoById(id);
        return goodsvo;
    }

    public void reduceStock(goodsVo goodsVo) {
        MiaoshaGoods g = new MiaoshaGoods();
        g.setGoodsId(goodsVo.getId());
        goodsDao.reduceStock(g);
    }
}
