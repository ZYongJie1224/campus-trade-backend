package com.nuomi.backend.controller;

import com.alibaba.fastjson.JSONObject;
import com.nuomi.backend.model.Product;
import com.nuomi.backend.model.QuickResponse;
import com.nuomi.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/products")
public class ProductController {
    @Autowired
    ProductService productService;
    QuickResponse quickResponse = new QuickResponse();


    //批量获取商品信息，用于主页展示
    @RequestMapping(value = "/ProductsBatch",method = RequestMethod.GET)
    public JSONObject getProducts(  @RequestParam(required = false, defaultValue = "0") Long lastId,
                                    @RequestParam(required = false, defaultValue = "40") Integer pageSize) {
        return quickResponse.getQuickDataResponse(200,productService.ProductDetailVOList(lastId,pageSize),"获取成功");
    }

}
