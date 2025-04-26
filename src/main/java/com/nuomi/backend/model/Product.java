package com.nuomi.backend.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("products")
public class Product {
    @TableId(value = "product_id", type = IdType.AUTO)
    private Integer productId;

    @TableField("product_name")
    private String name;

    @TableField("product_title")
    private String title;

    @TableField(value = "product_price", update = "CAST(%s AS DECIMAL(10,2))")
    private BigDecimal price;

    @TableField("product_tags")
    private String tags;

    @TableField("product_img")
    private String imageUrl;

    @TableField("product_main_img")
    private String mainImageUrl;

    @TableField("create_user")
    private Long createUserId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    @TableField("delete_time")
    private LocalDateTime deleteTime;

    @TableField("category_id")
    private Integer categoryId;
}