package com.nuomi.backend.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nuomi.backend.mapper.NavigationMapper;

import com.nuomi.backend.model.Navigation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NavigationService {

    @Autowired
    private NavigationMapper navigationMapper;

    /**
     * 获取主导航数据
     *
     * @return 主导航列表
     */
    public List<Navigation> getMainNavigation() {
        QueryWrapper<Navigation> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("parent_id").orderByAsc("order_index");
        return navigationMapper.selectList(queryWrapper);
    }

    /**
     * 获取分类导航数据
     *
     * @return 分类导航列表
     */
    public List<Navigation> getCategoryNavigation() {
        QueryWrapper<Navigation> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNotNull("parent_id").orderByAsc("order_index");
        return navigationMapper.selectList(queryWrapper);
    }
}