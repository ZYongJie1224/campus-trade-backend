package com.nuomi.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nuomi.backend.model.UserFollow;

public interface UserFollowService extends IService<UserFollow> {
    int countFans(Long userId);
    int countFollows(Long userId);
    boolean follow(Long userId, Long followUserId);
    boolean unfollow(Long userId, Long followUserId);
    boolean isFollowing(Long userId, Long followUserId);
}