package com.nuomi.backend.controller;

import com.nuomi.backend.model.Const;
import com.nuomi.backend.model.Product;
import com.nuomi.backend.model.QuickResponse;
import com.nuomi.backend.service.ProductService;
import com.nuomi.backend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(value = "/api/products")
public class ProductController {
    @Autowired
    ProductService productService;

    // 批量获取商品信息，用于主页展示
    @GetMapping("/ProductsBatch")
    public QuickResponse<?> getProducts(@RequestParam(required = false, defaultValue = "0") Long lastId,
                                        @RequestParam(required = false, defaultValue = "40") Integer pageSize) {
        System.out.println("lastId: " + lastId);
        System.out.println("pageSize: " + pageSize);
        return QuickResponse.success(productService.ProductDetailVOList(lastId, pageSize), "获取成功");
    }

    // 新增：商品搜索接口
    @GetMapping("/search")
    public QuickResponse<?> searchProducts(@RequestParam("keyword") String keyword,
                                           @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                           @RequestParam(required = false, defaultValue = "20") Integer pageSize) {
        return QuickResponse.success(
                productService.searchProductList(keyword, pageNum, pageSize),
                "搜索成功"
        );
    }
    @PostMapping(value = "/uploadProduct")
    public QuickResponse<Product>  uploadProduct(
            @RequestParam("product_name") String productName,
            @RequestParam("product_title") String productTitle,
            @RequestParam("product_description") String productDescription,
            @RequestParam("product_price") BigDecimal productPrice,
            @RequestParam(value = "tags[]", required = false) List<String> tags,
            @RequestParam("main_image") MultipartFile mainImage,
            @RequestParam(value = "other_images[]", required = false) List<MultipartFile> otherImages,
            @RequestParam("category_id") Integer categoryId,
//            @RequestParam("create_user") Long createUserId,
            HttpServletRequest request    ) {
        String token = request.getHeader("Authorization");
        if (StringUtils.isEmpty(token)) {
            return QuickResponse.fail(Const.FAIL, "未登录，请先登录");
        }
        try {

            Product product = productService.createProduct(
                    productName, productTitle, productDescription, productPrice, tags, mainImage, otherImages, categoryId, JwtUtil.getUserId(token)
            );
            return QuickResponse.success(product, "商品上传成功");
        } catch (IllegalArgumentException ex) {
            return QuickResponse.fail(400, "参数错误: " + ex.getMessage());
        } catch (RuntimeException ex) {
            return QuickResponse.fail(500, "服务器错误: " + ex.getMessage());
        } catch (Exception ex) {
            return QuickResponse.fail(500, "未知错误: " + ex.getMessage());
        }
    }

//    @RequestMapping(value = "/getProductById",method = RequestMethod.GET)
    @GetMapping("/{id}")
    public QuickResponse<?> getProductById(@PathVariable("id") Long id) {
        Product product = productService.selectById(id);
        if (product != null) {
            return QuickResponse.success(product, "获取商品详情成功");
        } else {
            return QuickResponse.fail(404, "商品未找到");
        }
    }


}