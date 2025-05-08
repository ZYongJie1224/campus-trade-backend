package com.nuomi.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nuomi.backend.model.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}