package com.nuomi.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nuomi.backend.mapper.ProductMapper;
import com.nuomi.backend.model.Product;
import com.nuomi.backend.model.VO.ProductDetailVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService extends IService<Product> {
    List<ProductDetailVO> ProductDetailVOList(@Param("lastId") Long lastId,
                                              @Param("pageSize") Integer pageSize);
    boolean saveProduct(String productName, String productTitle, String productDescription,
                        String productPrice, List<String> tags, MultipartFile mainImage,
                        List<MultipartFile> otherImages);
    Product createProduct(
            String productName,
            String productTitle,
            String productDescription,
            BigDecimal productPrice,
            List<String> tags,
            MultipartFile mainImage,
            List<MultipartFile> otherImages,
            Integer categoryId,
            Long createUserId
    );
    Product selectById(Long id);
    List<String> selectProductImgById(Long productId);
    List<ProductDetailVO> searchProductList(String keyword, Integer pageNum, Integer pageSize);
}
