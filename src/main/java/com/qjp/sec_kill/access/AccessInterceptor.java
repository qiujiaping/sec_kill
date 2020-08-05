package com.qjp.sec_kill.access;

import com.alibaba.fastjson.JSON;
import com.qjp.sec_kill.domain.MiaoshaUser;
import com.qjp.sec_kill.redis.AccessKey;
import com.qjp.sec_kill.redis.RedisService;
import com.qjp.sec_kill.result.CodeMsg;
import com.qjp.sec_kill.result.Result;
import com.qjp.sec_kill.service.MiaoshaUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * description: AccessInterceptor
 * date: 2020/6/8 17:35
 * author: 雨夜微凉
 * version: 1.0
 */
@Service
public class AccessInterceptor extends HandlerInterceptorAdapter {
    //限流+通过token获取用户的拦截器，对标有访问AccessLimit注解的方法做出限制，需要登录，且对标有注解方法作缓存
    @Autowired
    MiaoshaUserService userService;
    @Autowired
    RedisService redisService;
    private static Logger log = LoggerFactory.getLogger(AccessInterceptor.class);

    @Override//作用有2个：1个是拦截作为通过客户端的cookie获得token通过服务器得到用户。
    // 2：查看处理器方法的是否有注解AccessLimit，若有作限流，若无，则无视直接返回。
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if(handler instanceof HandlerMethod) {
            MiaoshaUser user = getUser(request, response);
            log.info(user.toString());
            UserContext.setUser(user);
            HandlerMethod hm = (HandlerMethod)handler;
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            //找到标有注解AccessLimit的类的方法，若该处理器方法没有标则直接放行，否则继续处理
            if(accessLimit == null) {
                return true;
            }
            //获得AccessLimit注解的属性
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();
            String key = request.getRequestURI();
            //判断为需要登录的方法，如果没有登录则返回错误信息，并不做进一步转发（拦截）
            if(needLogin) {
                //未登录，返回错误信息，并不做进一步转发
                if(user == null) {
                    render(response, CodeMsg.SESSION_ERROR);
                    return false;
                }
                //登录则设置访问的url加上用户id
                key += "_" + user.getId();
            }else {
                //若不需要登录
                //do nothing
            }

            AccessKey ak = AccessKey.withExpire(seconds);
            Integer count = redisService.get(ak, key, Integer.class);
            //若未存url则在redis当中缓存
            if(count  == null) {
                redisService.set(ak, key, 1);
            }
            //每点击一次在缓存当中累计一次
            else if(count < maxCount) {
                redisService.incr(ak, key);
            }
            //当达到最大点击访问数则响应错误
            else {
                render(response, CodeMsg.ACCESS_LIMIT_REACHED);
                return false;
            }
        }
        return true;
    }

    //若在访问接口的uri操作次数过于频繁则会返回错误提示
    private void render(HttpServletResponse response, CodeMsg message) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        String str  = JSON.toJSONString(Result.error(message));
        out.write(str.getBytes("UTF-8"));
        out.flush();
        out.close();
    }

    private MiaoshaUser getUser(HttpServletRequest request, HttpServletResponse response) {
        //可能放到请求域当中也可能放到请求域的cookie当中
        String paramToken = request.getParameter(MiaoshaUserService.COOKI_NAME_TOKEN);//请求域
        String cookieToken = getCookieValue(request, MiaoshaUserService.COOKI_NAME_TOKEN);//请求域的cookie
        if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
            return null;
        }
        String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
        return userService.getByToken(response, token);
    }

    private String getCookieValue(HttpServletRequest request, String cookiName) {
        Cookie[]  cookies = request.getCookies();
        if(cookies == null || cookies.length <= 0){
            return null;
        }
        for(Cookie cookie : cookies) {
            if(cookie.getName().equals(cookiName)) {
                return cookie.getValue();
            }
        }
        return null;
    }

}
