package com.qjp.sec_kill.vo;

import com.qjp.sec_kill.validator.isMobile;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * description: LoginVo
 * date: 2020/5/18 20:18
 * author: 雨夜微凉
 * version: 1.0
 */
public class LoginVo {

    @NotNull
    @isMobile
    private String mobile;

    @NotNull
    @Length(min=32)
    private String password;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



}
