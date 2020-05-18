package com.qjp.sec_kill.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * description: MD5Util
 * date: 2020/5/18 20:25
 * author: 雨夜微凉
 * version: 1.0
 */
public class MD5Util {

    public static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }
    //客户端加盐
    private static final String salt = "123456789";

    //客服端第一次加密（输入表单传输加密）
    public static String inputPassToFormPass(String inputPass) {
        String str = ""+salt.charAt(0)+salt.charAt(2) + inputPass +salt.charAt(5) + salt.charAt(4);
      //  System.out.println(str);
        return md5(str);
    }

    //服务器端第二次加密（服务器接收到数据库）
    public static String formPassToDBPass(String formPass, String salt) {
        String str = ""+salt.charAt(0)+salt.charAt(2) + formPass +salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    //实例两次加密
    public static String inputPassToDbPass(String inputPass, String saltDB) {
        String formPass = inputPassToFormPass(inputPass);
        String dbPass = formPassToDBPass(formPass, saltDB);
        return dbPass;
    }

    public static void main(String[] args) {
        //第一次加密
       // System.out.println(inputPassToFormPass("123456"));//d3b1294a61a07da9b49b6e22b2cbd7f9
        //第一次加第二次加密
		//System.out.println(formPassToDBPass(inputPassToFormPass("123456"), "1a2b3c4d"));  //0c355321c8c94b126945a84805f92cce
        //第一次加第二次加密
        System.out.println(inputPassToDbPass("15623285319", "123456789"));    //0c355321c8c94b126945a84805f92cce
    }

}
