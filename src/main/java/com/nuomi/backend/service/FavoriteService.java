package com.nuomi.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nuomi.backend.model.Favorites;


public interface FavoriteService extends IService<Favorites> {
    int countByUserId(Long userId);
    boolean isFavorited(Long userId, Long productId);
    boolean removeFavorite(Long userId, Long productId);
}