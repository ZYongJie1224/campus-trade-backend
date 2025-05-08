package com.nuomi.backend.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.nuomi.backend.mapper.UserFollowMapper;
import com.nuomi.backend.model.UserFollow;
import com.nuomi.backend.service.UserFollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserFollowServiceImpl extends ServiceImpl<UserFollowMapper, UserFollow> implements UserFollowService {
    @Autowired
    private UserFollowMapper userFollowMapper;

    @Override
    public int countFans(Long userId) {
        return userFollowMapper.countFans(userId);
    }

    @Override
    public int countFollows(Long userId) {
        return userFollowMapper.countFollows(userId);
    }

    @Override
    public boolean follow(Long userId, Long followUserId) {
        UserFollow uf = new UserFollow();
        uf.setUserId(userId);
        uf.setFollowUserId(followUserId);
        // 避免重复关注
        if(userFollowMapper.isFollowing(userId, followUserId) > 0) return false;
        return this.save(uf);
    }

    @Override
    public boolean unfollow(Long userId, Long followUserId) {
        return this.lambdaUpdate()
                .eq(UserFollow::getUserId, userId)
                .eq(UserFollow::getFollowUserId, followUserId)
                .remove();
    }

    @Override
    public boolean isFollowing(Long userId, Long followUserId) {
        return userFollowMapper.isFollowing(userId, followUserId) > 0;
    }
}