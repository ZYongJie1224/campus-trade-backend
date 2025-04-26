package com.nuomi.backend.model.VO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDetailVO {
    // 商品基础信息
//    private Integer productId;
    private String productName;
    private String productTitle;
    private BigDecimal productPrice;
    private String productMainImg;
    private String productDescription;
    private int wants;


    // 分类信息
//    private Integer categoryId;
    private String categoryName;

    // 创建者信息
    private String nickname;
//    private String creatorName;
    private String avatarUrl;
    private int isVerified;
//    private String creatorEmail;
}