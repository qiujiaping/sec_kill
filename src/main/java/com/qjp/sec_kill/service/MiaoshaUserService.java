package com.qjp.sec_kill.service;

import com.qjp.sec_kill.dao.MiaoshaUserDao;
import com.qjp.sec_kill.domain.MiaoshaUser;
import com.qjp.sec_kill.exception.GlobalException;
import com.qjp.sec_kill.redis.MiaoshaUserKey;
import com.qjp.sec_kill.redis.RedisService;
import com.qjp.sec_kill.result.CodeMsg;
import com.qjp.sec_kill.util.MD5Util;
import com.qjp.sec_kill.util.UUIDUtil;
import com.qjp.sec_kill.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
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

    public static final String COOKI_NAME_TOKEN = "token";


    //通过id获得用户（对象级缓存）先从缓存中查若没有，从数据库查，
    public MiaoshaUser getById(Long id) {
        //取缓存(3：对象级缓存（细粒度的）2：url缓存，1：页面缓存)
        MiaoshaUser user = redisService.get(MiaoshaUserKey.getById, ""+id, MiaoshaUser.class);
        if(user != null) {
            return user;
        }
        //取数据库
        user = miaoshaUserDao.getById(id);
        if(user != null) {
            redisService.set(MiaoshaUserKey.getById, ""+id, user);
        }
        return user;
    }

    //登录
    public boolean login(HttpServletResponse response, LoginVo loginVo) {
        //判断vo是否正常
        if(loginVo==null){
            throw new GlobalException(CodeMsg.SERVER_ERROR);//抛出自定义的全局异常，可被全局异常处理器捕捉
        }
        String mobile = loginVo.getMobile();
        String password1 = loginVo.getPassword();//客户端经一次加密的密码
        //1：通过id获得用户，判断用户是否存在
        MiaoshaUser user = getById(Long.valueOf(mobile));
        if(user==null){
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);//抛出自定义的全局异常，可被全局异常处理器捕捉
        }
        //2：获得用户密码，验证密码
        String password2 = user.getPassword();//对第一次加密的密码再经一次加密的密码
        String salt = user.getSalt();
        String calpass = MD5Util.formPassToDBPass(password1, salt);

        if(!calpass.equals(password2)){
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);//抛出自定义的全局异常，可被全局异常处理器捕捉
        }
        //3：登录成功后把用户信息添加到缓存，即做类session的操作

        //分布式的session:把token信息存到第三方缓存，生成token放到cookie当中
        String token= UUIDUtil.uuid();//生成cookie
        System.out.println("token:"+token);
        addCookie(response,token,user);
        return true;
    }

    //
    private void addCookie(HttpServletResponse response,String token,MiaoshaUser user){
        boolean flag = redisService.set(MiaoshaUserKey.token, token, user);// className（MiaoshaUserKey/UserKey）+":" + prefix（tk/id/name）+key/token
        Cookie cookie = new Cookie(COOKI_NAME_TOKEN, token);
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        cookie.setPath("/");
        System.out.println(cookie);
        response.addCookie(cookie);
    }

    //通过token 得到秒杀用户
    public MiaoshaUser getByToken(HttpServletResponse response,String token){
        if(token==null){
            return null;
        }
        MiaoshaUser miaoshaUser = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
        //延长有效期
        if(miaoshaUser != null) {
            addCookie(response, token, miaoshaUser);
        }
        return miaoshaUser;


    }



}
