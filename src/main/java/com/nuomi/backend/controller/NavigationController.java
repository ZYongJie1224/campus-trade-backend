package com.nuomi.backend.controller;

import com.nuomi.backend.model.Navigation;
import com.nuomi.backend.model.QuickResponse;
import com.nuomi.backend.service.NavigationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nav")
public class NavigationController {

    @Autowired
    private NavigationService navigationService;

    /**
     * 获取主导航（返回统一QuickResponse结构）
     */
    @GetMapping("/main")
    public QuickResponse<List<Navigation>> getMainNavigation() {
        List<Navigation> navList = navigationService.getMainNavigation();
        return QuickResponse.success(navList, "获取主导航成功");
    }

    /**
     * 获取分类导航（返回统一QuickResponse结构）
     */
    @GetMapping("/categories")
    public QuickResponse<List<Navigation>> getCategoryNavigation() {
        List<Navigation> navList = navigationService.getCategoryNavigation();
        return QuickResponse.success(navList, "获取分类导航成功");
    }
}