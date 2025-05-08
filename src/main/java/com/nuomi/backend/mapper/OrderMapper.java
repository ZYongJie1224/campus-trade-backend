package com.nuomi.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nuomi.backend.model.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
    @Select("SELECT COUNT(*) FROM orders WHERE buyer_id = #{buyerId}")
    int countByBuyerId(Long buyerId);

    @Select("SELECT COUNT(*) FROM orders WHERE seller_id = #{sellerId}")
    int countBySellerId(Long sellerId);

    @Select("SELECT * FROM `orders` WHERE order_id = #{orderId} LIMIT 1")
    Order selectByOrderId(String orderId);}