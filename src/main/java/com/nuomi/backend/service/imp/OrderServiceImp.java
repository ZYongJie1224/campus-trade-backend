package com.nuomi.backend.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuomi.backend.mapper.OrderMapper;
import com.nuomi.backend.model.Order;
import com.nuomi.backend.model.Product;
import com.nuomi.backend.model.User;
import com.nuomi.backend.service.OrderService;
import com.nuomi.backend.service.ProductService;
import com.nuomi.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class OrderServiceImp extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ProductService productService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private UserService userService;

    @Override
    public int countByBuyerId(Long buyerId) {
        return orderMapper.countByBuyerId(buyerId);
    }

    @Override
    public int countBySellerId(Long sellerId) {
        return orderMapper.countBySellerId(sellerId);
    }

    @Override
    @Transactional
    public Order createOrder(Long buyerId, Long productId, String address) {
        Product product = productService.getById(productId);
        User buyeruser = userService.getById(buyerId);
        if (product == null) {
            throw new IllegalArgumentException("商品不存在");
        }
        if ((product.getIsLocked() != null && product.getIsLocked() == 1)
                || (product.getIsSold() != null && product.getIsSold() == 1)) {
            throw new IllegalArgumentException("商品已售出或正在结算中");
        }
        // 锁定商品
        User selleruser = userService.getById(product.getCreateUserId());
        product.setIsLocked(1);
        productService.updateById(product);

        // 创建订单
        Order order = new Order();
        order.setOrderId(UUID.randomUUID().toString().replaceAll("-", ""));
        order.setProductName(product.getName());
        order.setBuyerName(buyeruser.getUsername());// 买的人
        order.setSellerName(selleruser.getUsername());
        order.setBuyerId(buyerId);
        order.setSellerId(product.getCreateUserId());
        order.setProductId(productId);
        order.setOrderPrice(product.getPrice());
        order.setAddress(address);
        order.setOrderStatus(0); // 0:待付款
        Date now = new Date();
        order.setCreateTime(now);
        order.setUpdateTime(now);
        // 设置支付截止时间
        Date deadline = new Date(now.getTime() + 15 * 60 * 1000);
        order.setPaymentDeadline(deadline);
        orderMapper.insert(order);

        // Redis 写入超时key，15分钟自动释放
        String redisKey = "order:unpaid:" + order.getOrderId();
        redisTemplate.opsForValue().set(redisKey, order.getId().toString(), 15, TimeUnit.MINUTES);
        return order;
    }

    @Override
    @Transactional
    public void payOrder(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null || order.getOrderStatus() != 0) {
            throw new IllegalArgumentException("订单不存在或状态异常");
        }
        order.setOrderStatus(1); // 1:已付款
        order.setUpdateTime( new Date());
        orderMapper.updateById(order);

        // 标记商品已售出并解锁
        Product product = productService.getById(order.getProductId());
        product.setIsSold(1);
        product.setIsLocked(0);
        productService.updateById(product);

        // 删除Redis超时key
        redisTemplate.delete("order:unpaid:" + order.getOrderId());
    }

    @Override
    @Transactional
    public void cancelUnpaidOrder(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null || order.getOrderStatus() != 0) {
            return;
        }
        order.setOrderStatus(2); // 2:已取消
        order.setUpdateTime(new Date());
        orderMapper.updateById(order);

        // 解锁商品
        Product product = productService.getById(order.getProductId());
        if (product != null) {
            product.setIsLocked(0);
            productService.updateById(product);
        }
    }

    @Override
    public Order getOrderByOrderId(String orderId) {
        return orderMapper.selectByOrderId(orderId);
    }

    @Override
    public IPage<Order> getBuyOrdersByTab(Long userId, String tab, Integer page, Integer pageSize) {
        LambdaQueryWrapper<Order> qw = new LambdaQueryWrapper<>();
        qw.eq(Order::getBuyerId, userId);
        switch (tab) {
            case "pendingPay": qw.eq(Order::getOrderStatus, 0); break;
            case "pendingReceive": qw.eq(Order::getOrderStatus, 1); break;
            // 其他tab可扩展
            default: break;
        }
        return orderMapper.selectPage(new Page<>(page, pageSize), qw);
    }

    @Override
    public IPage<Order> getSellOrdersByTab(Long userId, String tab, Integer page, Integer pageSize) {
        LambdaQueryWrapper<Order> qw = new LambdaQueryWrapper<>();
        qw.eq(Order::getSellerId, userId);
//        if (keyword != null && !keyword.isEmpty()) {
//            qw.and(w -> w.like(Order::getOrderId, keyword)
//                    .or()
//                    .like(Order::getProductName, keyword));
//        }
        switch (tab) {
            case "pendingPay": qw.eq(Order::getOrderStatus, 0); break;
            case "pendingReceive": qw.eq(Order::getOrderStatus, 1); break;
            // 其他tab可扩展
            default: break;
        }
        return orderMapper.selectPage(new Page<>(page, pageSize), qw);
    }




}