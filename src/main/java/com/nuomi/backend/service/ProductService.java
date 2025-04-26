package com.nuomi.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nuomi.backend.mapper.ProductMapper;
import com.nuomi.backend.model.Product;
import com.nuomi.backend.model.VO.ProductDetailVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductService extends IService<Product> {
    List<ProductDetailVO> ProductDetailVOList(@Param("lastId") Long lastId,
                                              @Param("pageSize") Integer pageSize);
}
