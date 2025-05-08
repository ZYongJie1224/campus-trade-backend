package com.nuomi.backend.service.imp;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.nuomi.backend.mapper.FavoriteMapper;
import com.nuomi.backend.model.Favorites;
import com.nuomi.backend.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FavoriteServiceImp extends ServiceImpl<FavoriteMapper, Favorites> implements FavoriteService {
    @Autowired
    private FavoriteMapper favoriteMapper;

    @Override
    public int countByUserId(Long userId) {
        return favoriteMapper.countByUserId(userId);
    }

    @Override
    public boolean isFavorited(Long userId, Long productId) {
        return count(new QueryWrapper<Favorites>()
                .eq("user_id", userId)
                .eq("product_id", productId)
        ) > 0;
    }

    @Override
    public boolean removeFavorite(Long userId, Long productId) {
        return remove(new QueryWrapper<Favorites>()
                .eq("user_id", userId)
                .eq("product_id", productId)
        );
    }
}
