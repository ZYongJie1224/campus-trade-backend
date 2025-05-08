package com.nuomi.backend.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nuomi.backend.model.Order;
import com.nuomi.backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {
    @Autowired
    private OrderService orderService;

    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        if (expiredKey.startsWith("order:unpaid:")) {
            String orderId = expiredKey.substring("order:unpaid:".length());
            // 查询数据库orderId对应的订单
            Order order = orderService.getOne(new QueryWrapper<Order>().eq("order_id", orderId));
            if (order != null) {
                orderService.cancelUnpaidOrder(order.getId());
            }
        }
    }
}