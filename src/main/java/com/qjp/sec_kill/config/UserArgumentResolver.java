package com.qjp.sec_kill.config;


import com.qjp.sec_kill.access.UserContext;
import com.qjp.sec_kill.domain.MiaoshaUser;
import com.qjp.sec_kill.service.MiaoshaUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * description: 最后一次的修改在这里做了简化，把请求放到线程域当中
 * date: 2020/5/19 10:49
 * author: 雨夜微凉
 * version: 1.0
 */
@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    MiaoshaUserService miaoshaUserService;


    //这一步很重要，判断控制器方法是否支持这个方法的自动绑定，如自己的domian的普通绑定和model和request，response可以直接写到控制器方法形参
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> clazz = methodParameter.getParameterType();
        String simpleName = clazz.getSimpleName();
        System.out.println("simpleName:"+simpleName);
        return clazz== MiaoshaUser.class;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        MiaoshaUser user = UserContext.getUser();
        return user;
    }

}
