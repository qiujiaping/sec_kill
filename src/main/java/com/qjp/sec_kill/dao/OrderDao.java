package com.qjp.sec_kill.dao;

import com.qjp.sec_kill.domain.MiaoshaOrder;
import com.qjp.sec_kill.domain.MiaoshaUser;
import com.qjp.sec_kill.domain.OrderInfo;
import com.qjp.sec_kill.vo.goodsVo;
import org.apache.ibatis.annotations.*;

/**
 * description: OrderDao
 * date: 2020/5/24 23:42
 * author: 雨夜微凉
 * version: 1.0
 */

@Mapper
public interface OrderDao {
    @Select("select * from miaosha_order where user_id=#{userId} and goods_id=#{goodsId}")
    public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(@Param("userId")long userId, @Param("goodsId")long goodsId);


    //生成订单信息并返回主键
    @Insert("insert into order_info(user_id, goods_id, goods_name, goods_count, goods_price, order_channel, status, create_date)values("
            + "#{userId}, #{goodsId}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{orderChannel},#{status},#{createDate} )")
    @SelectKey(keyColumn="id", keyProperty="id", resultType=long.class, before=false, statement="select last_insert_id()")
    long insertOrderInfo(OrderInfo orderInfo);

    @Insert("insert into miaosha_order (user_id, goods_id, order_id)values(#{userId}, #{goodsId}, #{orderId})")
    void insertMiaoshaOrder(MiaoshaOrder miaoshaOrder);
    @Select("select * from order_info where id=#{orderId}")
    OrderInfo getOrderById(@Param("orderId") long orderId);
}
