package com.nuomi.backend.service.imp;

import com.nuomi.backend.mapper.CategoryMapper;
import com.nuomi.backend.model.Category;
import com.nuomi.backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImp implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> listAllCategories() {
        return categoryMapper.selectList(null);
    }
}