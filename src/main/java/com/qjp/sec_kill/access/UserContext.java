package com.qjp.sec_kill.access;

import com.qjp.sec_kill.domain.MiaoshaUser;

/**
 * description: 线程作用域，线程独有区域的内存
 * date: 2020/6/8 17:45
 * author: 雨夜微凉
 * version: 1.0
 */

public class UserContext {
    private static ThreadLocal<MiaoshaUser> userHolder = new ThreadLocal<MiaoshaUser>();

    public static void setUser(MiaoshaUser user) {
        userHolder.set(user);
    }

    public static MiaoshaUser getUser() {
        return userHolder.get();
    }

}
