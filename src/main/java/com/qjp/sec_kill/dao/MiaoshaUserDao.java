package com.qjp.sec_kill.dao;

import com.qjp.sec_kill.domain.MiaoshaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * description: MiaoshaUserDao
 * date: 2020/5/18 20:44
 * author: 雨夜微凉
 * version: 1.0
 */
@Mapper
public interface MiaoshaUserDao {

    @Select("select * from miaosha_user where id=#{id}")
    public MiaoshaUser getById(@Param("id") long id);
}
