package com.qjp.sec_kill.controller;

import com.qjp.sec_kill.redis.RedisService;
import com.qjp.sec_kill.result.Result;
import com.qjp.sec_kill.service.UserService;
import com.qjp.sec_kill.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * description: 登录模块
 * date: 2020/5/18 20:10
 * author: 雨夜微凉
 * version: 1.0
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    private static Logger log = LoggerFactory.getLogger(LoginController.class);


    @Autowired
    RedisService redisService;
    @Autowired
    UserService userService;

    @RequestMapping("/to_login")
    public String toLogin(){
        return "login";
    }

//    @RequestMapping("/do_login")
//    @ResponseBody
//    public Result<Boolean> doLogin(HttpServletResponse response, @Valid LoginVo loginVo) {
//        log.info(loginVo.toString());
//        //登录
//        userService.login(response, loginVo);
//        return Result.success(true);
//    }


}
