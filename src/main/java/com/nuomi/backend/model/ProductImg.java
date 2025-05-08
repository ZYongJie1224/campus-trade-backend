package com.nuomi.backend.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("product_imgs")
public class ProductImg {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("product_id")
    private Integer productId;

    @TableField("img_url")
    private String imgUrl;
}