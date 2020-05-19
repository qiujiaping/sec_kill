package com.qjp.sec_kill.controller;

import com.qjp.sec_kill.domain.MiaoshaUser;
import com.qjp.sec_kill.service.GoodsService;
import com.qjp.sec_kill.vo.goodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * description: GoodsController
 * date: 2020/5/19 10:41
 * author: 雨夜微凉
 * version: 1.0
 */

@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    GoodsService goodsService;
    //到商品列表
    @RequestMapping("to_list")
    public String list(Model model, MiaoshaUser miaoshaUser){//为简化操作把参数的业务操作压缩到mvc框架处理，我们平时在方法参数里直接得到model和request，response就是这样的
        model.addAttribute("user",miaoshaUser);
        List<goodsVo> goodsVos = goodsService.listGoodsVo();
        model.addAttribute("goodsList",goodsVos);
        return "goods_list";
    }

}
