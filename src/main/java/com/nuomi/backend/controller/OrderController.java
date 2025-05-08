package com.nuomi.backend.controller;

import com.nuomi.backend.model.Order;
import com.nuomi.backend.model.Product;
import com.nuomi.backend.model.QuickResponse;
import com.nuomi.backend.service.OrderService;
import com.nuomi.backend.service.ProductService;
import com.nuomi.backend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @PostMapping("/create")
    public QuickResponse<Order> createOrder(
            @RequestParam("product_id") Long productId,
            @RequestParam("address") String addressId,
            HttpServletRequest request
    ) {
        String token = request.getHeader("Authorization");
        if (StringUtils.isEmpty(token)) {
            return QuickResponse.fail(401, "未登录，请先登录");
        }
        Long userId = JwtUtil.getUserId(token);

        // 查询商品
        Product product = productService.selectById(productId);
        if (product == null) {
            return QuickResponse.fail(400, "商品不存在");
        }
        // 判断是否为自己的商品
        if (userId.equals(product.getCreateUserId())) {
            return QuickResponse.fail(403, "不能购买自己的商品");
        }
        // 判断商品是否已卖出
        if (product.getIsSold() != null && product.getIsSold() == 1) {
            return QuickResponse.fail(400, "该商品已被卖出，无法下单");
        }

        try {
            Order order = orderService.createOrder(userId, productId, addressId);
            return QuickResponse.success(order, "下单成功");
        } catch (IllegalArgumentException ex) {
            return QuickResponse.fail(400, ex.getMessage());
        } catch (Exception ex) {
            return QuickResponse.fail(500, "服务器错误: " + ex.getMessage());
        }
    }
    // 获取订单详情
    @GetMapping("/detail/{orderId}")
    public QuickResponse<?> getOrderDetail(@PathVariable String orderId) {
        Order order = orderService.getOrderByOrderId(orderId);
        if (order == null) {
            return QuickResponse.fail(404, "订单不存在");
        }
        return QuickResponse.success(order, "订单详情获取成功");
    }

    // 订单支付接口（模拟支付）
    @PostMapping("/{orderId}/pay")
    public QuickResponse<?> payOrder(@PathVariable String orderId) {
        try {
            Order order = orderService.getOrderByOrderId(orderId);
            if (order == null) {
                return QuickResponse.fail(404, "订单不存在");
            }
            if (order.getOrderStatus() == 1) {
                return QuickResponse.fail(400, "订单已支付");
            }
            if (order.getOrderStatus() == 2) {
                return QuickResponse.fail(400, "订单已取消");
            }
            orderService.payOrder(order.getId()); // 这里直接调用支付逻辑（即状态置为已支付，商品设为已售出）
            return QuickResponse.success(null, "支付成功");
        } catch (Exception e) {
            return QuickResponse.fail(500, "服务器异常");
        }
    }
}