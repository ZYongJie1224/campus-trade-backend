package com.nuomi.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.nuomi.backend.model.UserFollow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserFollowMapper extends BaseMapper<UserFollow> {
    @Select("SELECT COUNT(*) FROM user_follow WHERE follow_user_id = #{userId}")
    int countFans(Long userId);

    @Select("SELECT COUNT(*) FROM user_follow WHERE user_id = #{userId}")
    int countFollows(Long userId);

    @Select("SELECT COUNT(*) FROM user_follow WHERE user_id = #{userId} AND follow_user_id = #{followUserId}")
    int isFollowing(Long userId, Long followUserId);
}