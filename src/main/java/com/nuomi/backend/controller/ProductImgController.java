package com.nuomi.backend.controller;

import com.nuomi.backend.model.QuickResponse;
import com.nuomi.backend.service.imp.ProductServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/product-imgs")
public class ProductImgController {
    @Autowired
    private ProductServiceImp productService;

    // GET /api/product-imgs/{productId}
    @GetMapping("/{productId}")
    public QuickResponse<List<String>> getProductImgs(@PathVariable Long productId) {
        List<String> imgUrls = productService.selectProductImgById(productId);
        return QuickResponse.success(imgUrls, "获取商品附加图片成功");
    }
}