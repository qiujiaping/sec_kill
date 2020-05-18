package com.qjp.sec_kill.service;

import com.qjp.sec_kill.dao.MiaoshaUserDao;
import com.qjp.sec_kill.domain.MiaoshaUser;
import com.qjp.sec_kill.exception.GlobalException;
import com.qjp.sec_kill.redis.RedisService;
import com.qjp.sec_kill.result.CodeMsg;
import com.qjp.sec_kill.util.MD5Util;
import com.qjp.sec_kill.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

/**
 * description: MiaoshaUserService
 * date: 2020/5/18 20:39
 * author: 雨夜微凉
 * version: 1.0
 */
@Service
public class MiaoshaUserService {
    @Autowired
    MiaoshaUserDao miaoshaUserDao;

    @Autowired
    RedisService redisService;


    //通过id获得用户
    public MiaoshaUser getById(Long id) {
        return miaoshaUserDao.getById(id);
    }

    //登录
    public boolean login(HttpServletResponse response, LoginVo loginVo) {
        //判断vo是否正常
        if(loginVo==null){
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String password1 = loginVo.getPassword();//客户端经一次加密的密码
        //通过id获得用户，判断用户是否存在
        MiaoshaUser user = getById(Long.valueOf(mobile));
        if(user==null){
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //获得用户密码，验证密码
        String password2 = user.getPassword();//对第一次加密的密码再经一次加密的密码
        String salt = user.getSalt();
        String calpass = MD5Util.formPassToDBPass(password1, salt);
        System.out.println(calpass);
        System.out.println(password2);
        if(!calpass.equals(password2)){
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        System.out.println("success! ");
        return true;



    }





}
