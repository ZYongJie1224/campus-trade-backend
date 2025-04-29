package com.nuomi.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nuomi.backend.model.Product;
import com.nuomi.backend.model.VO.ProductDetailVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ProductMapper extends BaseMapper<Product> {
    @Select("""
        SELECT 
            p.product_id, p.product_name, p.product_price,p.product_title,p.product_main_img,p.product_description,p.wants,
            c.category_name AS category_name,
            u.is_verified AS is_verified,
            u.nickname AS nickname,
            u.avatar_url AS avatar_url
        FROM products p
        LEFT JOIN categories c ON p.category_id = c.category_id
        LEFT JOIN users u ON p.create_user = u.user_id
        WHERE p.product_id > #{lastId} 
          AND p.is_deleted = 0
          AND u.status = 1
        ORDER BY p.product_id ASC
        LIMIT #{pageSize}
        """)
    List<ProductDetailVO> selectProductFeed(
            @Param("lastId") Long lastId,
            @Param("pageSize") Integer pageSize);
}
