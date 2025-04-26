package com.nuomi.backend.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuomi.backend.mapper.ProductMapper;
import com.nuomi.backend.model.Product;
import com.nuomi.backend.model.VO.ProductDetailVO;
import com.nuomi.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImp extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    ProductMapper productMapper;
    @Override
    public List<ProductDetailVO> ProductDetailVOList(Long lastId, Integer pageSize) {
        return productMapper.selectProductFeed(lastId, pageSize);
    }
}
