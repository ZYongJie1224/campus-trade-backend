package com.nuomi.backend.service;

import com.nuomi.backend.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface UserService {
    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 匹配的用户对象，登录失败返回null
     */
    User login(String username, String password);

    /**
     * 上传并更新用户头像
     * @param file 头像文件
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean upAdavatar(MultipartFile file, int userId) throws IOException;
    User getById(Long userId);
}