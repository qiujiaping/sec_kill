package com.qjp.sec_kill.controller;

import com.qjp.sec_kill.domain.MiaoshaUser;
import com.qjp.sec_kill.redis.GoodsKey;
import com.qjp.sec_kill.redis.RedisService;
import com.qjp.sec_kill.result.Result;
import com.qjp.sec_kill.service.GoodsService;
import com.qjp.sec_kill.vo.GoodsDetailVo;
import com.qjp.sec_kill.vo.goodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    @Autowired
    RedisService redisService;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;



    //到商品列表
//    @RequestMapping("to_list")
//    public String list(Model model, MiaoshaUser miaoshaUser){//为简化操作把参数的业务操作压缩到mvc框架处理，我们平时在方法参数里直接得到model和request，response就是这样的
//        model.addAttribute("user",miaoshaUser);
//        List<goodsVo> goodsVos = goodsService.listGoodsVo();
//        model.addAttribute("goodsList",goodsVos);
//        return "goods_list";
//    }

    //手动渲染+页面缓存
    @RequestMapping(value="/to_list", produces="text/html")
    @ResponseBody
    public String list(HttpServletRequest request, HttpServletResponse response, Model model, MiaoshaUser user) {
        model.addAttribute("user", user);
        //取缓存
        String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
        if(!StringUtils.isEmpty(html)) {
            return html;
        }
        List<goodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsList);
        WebContext springWebContext = new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());
         html = thymeleafViewResolver.getTemplateEngine().process("goods_list", springWebContext);
         //页面缓存在redis当中
        if(!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsList, "", html);
        }
        return html;
    }


    //商品详情页
//    @RequestMapping("to_detail/{id}")
//    public String to_detail(Model model, MiaoshaUser miaoshaUser, @PathVariable("id")Long id){//为简化操作把参数的业务操作压缩到mvc框架处理，我们平时在方法参数里直接得到model和request，response就是这样的
//
//        model.addAttribute("user",miaoshaUser);
//        goodsVo goodsvo = goodsService.getGoodsVoById(id);
//
//        model.addAttribute("goodsVo",goodsvo);
//        long starttime = goodsvo.getStartDate().getTime();
//        long endtime = goodsvo.getEndDate().getTime();
//        long currentTime = System.currentTimeMillis();
//        int sec_killStatus=0;
//        int remainTime=0;
//        if(currentTime<starttime){//秒杀还未开始
//            sec_killStatus=0;
//            remainTime=(int)(starttime-currentTime)/1000;
//
//        }
//        else if(currentTime>endtime){//秒杀已经结束
//            sec_killStatus=2;
//            remainTime=-1;
//        }
//        else {//秒杀正在进行当中
//            sec_killStatus=2;
//            remainTime=0;
//        }
//        model.addAttribute("sec_killStatus",sec_killStatus);
//        model.addAttribute("remainTime",remainTime);
//        return "goods_detail";
//    }
    //手动渲染+页面缓存
//    @RequestMapping(value="/to_detail2/{goodsId}",produces="text/html")
//    @ResponseBody
//    public String detail2(HttpServletRequest request, HttpServletResponse response, Model model,MiaoshaUser user,
//                          @PathVariable("goodsId")long goodsId) {
//        model.addAttribute("user", user);
//
//        //取缓存
//        String html = redisService.get(GoodsKey.getGoodsDetail, ""+goodsId, String.class);
//        if(!StringUtils.isEmpty(html)) {
//            return html;
//        }
//        //手动渲染
//        goodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
//        model.addAttribute("goodsVo", goods);
//
//        long startAt = goods.getStartDate().getTime();
//        long endAt = goods.getEndDate().getTime();
//        long now = System.currentTimeMillis();
//
//        int miaoshaStatus = 0;
//        int remainSeconds = 0;
//        if(now < startAt ) {//秒杀还没开始，倒计时
//            miaoshaStatus = 0;
//            remainSeconds = (int)((startAt - now )/1000);
//        }else  if(now > endAt){//秒杀已经结束
//            miaoshaStatus = 2;
//            remainSeconds = -1;
//        }else {//秒杀进行中
//            miaoshaStatus = 1;
//            remainSeconds = 0;
//        }
//        model.addAttribute("miaoshaStatus", miaoshaStatus);
//        model.addAttribute("remainSeconds", remainSeconds);
//
//
//        WebContext springWebContext = new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());
//
//        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", springWebContext);
//        if(!StringUtils.isEmpty(html)) {
//            redisService.set(GoodsKey.getGoodsDetail, ""+goodsId, html);
//        }
//        return html;
//    }

    /*
        前后端分离,页面静态化，当在html中点击连接后，
        直接到静态页面然后由ajax请求控制器接口获得数据返回，
        而不是由控制器处理解析后返回，
    */
    @RequestMapping(value="/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> detail(HttpServletRequest request, HttpServletResponse response, Model model, MiaoshaUser user,
                                        @PathVariable("goodsId")long goodsId) {
        goodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if(now < startAt ) {//秒杀还没开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int)((startAt - now )/1000);
        }else  if(now > endAt){//秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else {//秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        GoodsDetailVo vo = new GoodsDetailVo();
        vo.setGoods(goods);
        vo.setUser(user);
        vo.setRemainSeconds(remainSeconds);
        vo.setMiaoshaStatus(miaoshaStatus);
        return Result.success(vo);
    }
}
