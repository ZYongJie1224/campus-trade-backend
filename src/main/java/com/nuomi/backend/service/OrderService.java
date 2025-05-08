package com.nuomi.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nuomi.backend.model.Order;

import java.util.List;

public interface OrderService extends IService<Order> {
    int countByBuyerId(Long buyerId);
    int countBySellerId(Long sellerId);
    Order createOrder(Long buyerId, Long productId, String address);
    void payOrder(Long orderId);
    void cancelUnpaidOrder(Long orderId);

    Order getOrderByOrderId(String orderId);

    IPage<Order> getBuyOrdersByTab(Long userId, String tab, Integer page, Integer pageSize);
//    List<Order> getSellOrdersByTab(Long userId, String tab);
    IPage<Order> getSellOrdersByTab(Long userId, String tab, Integer page, Integer pageSize);

//    Order createOrder(Long buyerId, Long productId, Long addressId, Long orderPrice);
}
