package com.nuomi.backend.controller;

import com.nuomi.backend.model.Category;
import com.nuomi.backend.model.QuickResponse;
import com.nuomi.backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public QuickResponse<List<Category>> listAllCategories() {
        List<Category> categories = categoryService.listAllCategories();
        return QuickResponse.success(categories, "获取商品分类成功");
    }
}