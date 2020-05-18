package com.qjp.sec_kill.redis;

public interface KeyPrefix {

    public int expireSeconds();

    public String getPrefix();

}
