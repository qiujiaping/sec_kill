package com.qjp.sec_kill.service;

import com.qjp.sec_kill.domain.MiaoshaOrder;
import com.qjp.sec_kill.domain.MiaoshaUser;
import com.qjp.sec_kill.domain.OrderInfo;
import com.qjp.sec_kill.redis.MiaoshaKey;
import com.qjp.sec_kill.redis.RedisService;
import com.qjp.sec_kill.util.MD5Util;
import com.qjp.sec_kill.util.UUIDUtil;
import com.qjp.sec_kill.vo.goodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * description: MiaoshaService
 * date: 2020/5/24 23:55
 * author: 雨夜微凉
 * version: 1.0
 */
@Service
public class MiaoshaService {

    //为了在自己模块不引入命名不一致的dao，需要引入其他的service
    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;
    @Autowired
    RedisService redisService;


    @Transactional
    public OrderInfo miaosha(MiaoshaUser miaoshaUser, goodsVo goodsVo){
        //减库存 下订单(写入秒杀订单),返回订单信息
        boolean success = goodsService.reduceStock(goodsVo);
        //如果减库存成功则产生订单
        if(success){
            return  orderService.createOrder(miaoshaUser, goodsVo);
        }
        //否则秒杀失败（库存不足）
        else {
            setGoodsOver(goodsVo.getId());
            return null;
        }

    }

    public long getMIAOSHARESULT(Long useId, Long goodId) {
        MiaoshaOrder miaoshaOrder = orderService.getMiaoshaOrderByUserIdGoodsId(useId, goodId);
        //秒杀成功
        if(miaoshaOrder!=null){
            return miaoshaOrder.getOrderId();
        }
        //这里要区分是还在排队还是秒杀失败（库存不足）
        else {
            boolean isover=getGoodsOver(goodId);
            if(isover){
                return -1;
            }
            else return 0;
        }
    }

    private boolean getGoodsOver(Long goodId) {
        boolean exists = redisService.exists(MiaoshaKey.isGoodsOver, "" + goodId);
        return exists;
    }

    public void setGoodsOver(Long goodId){
        redisService.set(MiaoshaKey.isGoodsOver,""+goodId,true);
    }

    public String createMiaoshaPath(MiaoshaUser miaoshaUser, Long id) {
        if(id <=0) {
            return null;
        }
        //生成随机数并用
        String str = MD5Util.md5(UUIDUtil.uuid()+"123456");
        redisService.set(MiaoshaKey.getMiaoshaPath, ""+miaoshaUser.getId() + "_"+ id, str);
        return str;
    }

    /*检查随机路径变量*/
    public boolean checkPath(MiaoshaUser miaoshaUser, Long id, String path) {
        String pathOld = redisService.get(MiaoshaKey.getMiaoshaPath, "" + miaoshaUser.getId() + "_" + id, String.class);
        if(!path.equals(pathOld)){
            return false;
        }
        redisService.delete(MiaoshaKey.getMiaoshaPath, "" + miaoshaUser.getId() + "_" + id);
        return true;
    }

    /*产生验证码图片，并在图片生成表达式*/
    public BufferedImage createVerifyCode(MiaoshaUser user, long goodsId) {
        if(user == null || goodsId <=0) {
            return null;
        }
        int width = 80;
        int height = 32;
        //create the image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // set the background color
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        // draw the border
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        // create a random instance to generate the codes
        Random rdm = new Random();
        // make some confusion
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // generate a random code
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
       /*计算验证码表达式的结果*/
        int rnd = calc(verifyCode);
        //把验证码存到redis中
        redisService.set(MiaoshaKey.getMiaoshaVerifyCode, user.getId()+","+goodsId, rnd);
        //输出图片
        return image;
    }

    /*调用JavaScript的结果计算字符串表达式的结果*/
    private int calc(String verifyCode) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer)engine.eval(verifyCode);
        }catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * + - *
     * */
    private static char[] ops = new char[] {'+', '-', '*'};

    //产生表达式字符串
    private String generateVerifyCode(Random rdm) {
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(3)];
        char op2 = ops[rdm.nextInt(3)];
        String exp = ""+ num1 + op1 + num2 + op2 + num3;
        return exp;
    }

    public boolean checkVerifyCode(MiaoshaUser miaoshaUser, Long id, int verifyCode) {
        if(miaoshaUser==null||id<=0){
            return false;
        }
        Integer oldVerifyCode = redisService.get(MiaoshaKey.getMiaoshaVerifyCode, miaoshaUser.getId() + "," + id, Integer.class);
        //这里有为空的情况，因为机器人刷或者是直接在浏览器刷的时候（没有到）
        if(oldVerifyCode==null||oldVerifyCode-verifyCode!=0){
            return false;
        }
        redisService.delete(MiaoshaKey.getMiaoshaVerifyCode, miaoshaUser.getId()+","+id);
         return true;
    }
}
