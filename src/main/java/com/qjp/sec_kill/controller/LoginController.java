package com.qjp.sec_kill.controller;

import com.qjp.sec_kill.redis.RedisService;
import com.qjp.sec_kill.result.CodeMsg;
import com.qjp.sec_kill.result.Result;
import com.qjp.sec_kill.service.MiaoshaUserService;
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
@RequestMapping("/login")//来到登录页面
public class LoginController {

    private static Logger log = LoggerFactory.getLogger(LoginController.class);


    @Autowired
    RedisService redisService;
    @Autowired
    MiaoshaUserService userService;


    @RequestMapping("/to_login")
    public String toLogin(){
        return "login";
    }

    @RequestMapping("/do_login")//输入用户名密码后处理登录请求
    @ResponseBody//loginVo的作用是封装请求参数，并作校验
    public Result<Boolean> doLogin(HttpServletResponse response, @Valid LoginVo loginVo) {//如果这里校验未通过则这里会抛出参数绑定异常，
        // 哪个校验出错的参数会通过该参数的注解抛出错误信息
        log.info(loginVo.toString());
        //登录,异常的话把异常交给全局异常处理器，这样写的话就更简洁
        userService.login(response, loginVo);//如果调用该方法出现异常则在该方法里
                                            // 面就已经抛出了，能正常执行完这条语句表明是成功了
                                            //直接返回true就可以
        return Result.success(true);

    }


}
