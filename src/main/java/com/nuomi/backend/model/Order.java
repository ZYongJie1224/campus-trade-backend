package com.nuomi.backend.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@TableName("orders")
@Data
public class Order {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("product_name")
    private String productName;
    @TableField("seller_name")
    private String sellerName;
    @TableField("buyer_name")
    private String buyerName;
    @TableField("order_id")
    private String orderId;

    @TableField("buyer_id")
    private Long buyerId;

    @TableField("seller_id")
    private Long sellerId;

    @TableField("product_id")
    private Long productId;

    @TableField("order_price")
    private BigDecimal orderPrice;

    @TableField("address")
    private String address;

    @TableField("order_status")
    private Integer orderStatus; // 0-未付款, 1-已付款, 2-已完成, 3-已取消

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    @TableField(value = "payment_deadline", fill = FieldFill.INSERT_UPDATE)
    private Date paymentDeadline;
}