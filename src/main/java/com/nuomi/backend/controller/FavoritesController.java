package com.nuomi.backend.controller;


import com.nuomi.backend.model.Favorites;
import com.nuomi.backend.model.QuickResponse;
import com.nuomi.backend.service.FavoriteService;
import com.nuomi.backend.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/favorite")
@RequiredArgsConstructor
public class FavoritesController {
    private final FavoriteService favoriteService;

    @PostMapping("/add")
    public QuickResponse<Void> addFavorite(
            @RequestParam Long productId,
            HttpServletRequest request
    ) {
        String token = request.getHeader("Authorization");
        Long userId = JwtUtil.getUserId(token);
        if (userId == null) {
            return QuickResponse.fail(401, "请先登录");
        }
        if (favoriteService.isFavorited(userId, productId)) {
            return QuickResponse.fail(400, "已收藏");
        }

        Favorites favorite = new Favorites();
        favorite.setUserId(userId);
        favorite.setProductId(productId);
        favorite.setCreateTime(LocalDateTime.now());
        favoriteService.save(favorite);

        return QuickResponse.success("收藏成功");
    }

    @PostMapping("/remove")
    public QuickResponse<Void> removeFavorite(
            @RequestParam Long productId,
            HttpServletRequest request
    ) {
        String token = request.getHeader("Authorization");
        Long userId = JwtUtil.getUserId(token);
        if (userId == null) {
            return QuickResponse.fail(401, "请先登录");
        }
        boolean removed = favoriteService.removeFavorite(userId, productId);
        if (!removed) {
            return QuickResponse.fail(400, "未收藏");
        }
        return QuickResponse.success("取消收藏成功");
    }

    @GetMapping("/isFavorited")
    public QuickResponse<Boolean> isFavorited(
            @RequestParam Long productId,
            HttpServletRequest request
    ) {
        String token = request.getHeader("Authorization");
        Long userId = JwtUtil.getUserId(token);
        if (userId == null) {
            return QuickResponse.fail(401, "请先登录");
        }
        boolean favorited = favoriteService.isFavorited(userId, productId);
        return QuickResponse.success(favorited, "查询成功");
    }
}