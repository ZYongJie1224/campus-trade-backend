package com.nuomi.backend.controller;

import com.nuomi.backend.model.QuickResponse;
import com.nuomi.backend.model.User;
import com.nuomi.backend.model.Const;
import com.nuomi.backend.service.imp.UserServiceImp;
import com.nuomi.backend.utils.FileStorageService;
import com.nuomi.backend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserServiceImp userServiceImp;
    FileStorageService fileStorageService = new FileStorageService();

    /**
     * 登录接口，返回QuickResponse结构，data为用户信息
     */
    @PostMapping("/login")
    public QuickResponse<Map<String, Object>> login(@RequestBody User loginUser) {
        if (StringUtils.isEmpty(loginUser.getUsername()) || StringUtils.isEmpty(loginUser.getPassword())) {
            return QuickResponse.fail(Const.EMPTY_PARAM, "账号或密码不能为空");
        }
        User user = userServiceImp.login(loginUser.getUsername(), loginUser.getPassword());
        if (user == null) {
            return QuickResponse.fail(Const.ERROR_PARAM, "账号错误，或密码不正确");
        } else {
            // 生成JWT Token
            String token = JwtUtil.generateToken(user);
            // 构建返回信息
            Map<String, Object> result = new HashMap<>();
            result.put("user", user);
            result.put("token", token);
            return QuickResponse.success(result, "登录成功");
        }
    }

    /**
     * 获取当前登录用户详细信息（需前端请求header带Authorization: token）
     */
    @GetMapping("/me")
    public QuickResponse<User> getCurrentUser(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (StringUtils.isEmpty(token)) {
            return QuickResponse.fail(Const.FAIL, "未登录，请先登录");
        }
        Long userId = JwtUtil.getUserId(token);
        if (userId == null) {
            return QuickResponse.fail(Const.FAIL, "token无效或已过期");
        }
        User user = userServiceImp.getById(userId);
        if (user == null) {
            return QuickResponse.fail(Const.FAIL, "用户不存在");
        }
        return QuickResponse.success(user, "获取用户详情成功");
    }

    /**
     * 上传头像接口
     */
    @PostMapping("/upadavatar")
    public QuickResponse<String> upadavatar(@RequestBody MultipartFile file, @RequestHeader String token) throws IOException {
        int userId = Integer.parseInt(token.split("_")[0]);
        String fileName = file.getOriginalFilename();
        String fileSuffix = fileName.substring(fileName.lastIndexOf("."));
        if (!fileSuffix.equals(".jpg") && !fileSuffix.equals(".png") && !fileSuffix.equals(".jpeg")) {
            return QuickResponse.fail(Const.ERROR_PARAM, "文件类型不正确");
        }
        if (file.getSize() > 1024 * 1024 * 2) {
            return QuickResponse.fail(Const.ERROR_PARAM, "文件大小超过2MB");
        }
        if (userServiceImp.upAdavatar(file, userId)) {
            return QuickResponse.success("头像上传成功");
        } else {
            return QuickResponse.fail(Const.ERROR_PARAM, "头像上传失败");
        }
    }
}