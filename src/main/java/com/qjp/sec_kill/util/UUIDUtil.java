package com.qjp.sec_kill.util;

import sun.plugin.util.UIUtil;

import java.util.UUID;

/**
 * description: UUIDUtil
 * date: 2020/5/19 9:32
 * author: 雨夜微凉
 * version: 1.0
 */
public class UUIDUtil {
    public  static String uuid(){
        return UUID.randomUUID().toString().replace("-","");
    }

    public static void main(String[] args) {
        System.out.println(UUIDUtil.uuid());
    }

}
