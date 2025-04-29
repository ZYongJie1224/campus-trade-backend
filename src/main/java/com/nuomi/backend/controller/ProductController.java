package com.nuomi.backend.controller;

import com.nuomi.backend.model.QuickResponse;
import com.nuomi.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/products")
public class ProductController {
    @Autowired
    ProductService productService;

    // 批量获取商品信息，用于主页展示
    @GetMapping("/ProductsBatch")
    public QuickResponse<?> getProducts(@RequestParam(required = false, defaultValue = "0") Long lastId,
                                        @RequestParam(required = false, defaultValue = "40") Integer pageSize) {
        return QuickResponse.success(productService.ProductDetailVOList(lastId, pageSize), "获取成功");
    }
}