package com.nuomi.backend.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nuomi.backend.model.Order;
import com.nuomi.backend.model.QuickResponse;
import com.nuomi.backend.model.User;
import com.nuomi.backend.model.Const;
import com.nuomi.backend.service.OrderService;
import com.nuomi.backend.service.UserFollowService;
import com.nuomi.backend.service.imp.FavoriteServiceImp;
import com.nuomi.backend.service.imp.OrderServiceImp;
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
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserServiceImp userServiceImp;

    @Autowired
    OrderServiceImp orderServiceImp;

    @Autowired
    FavoriteServiceImp favoriteServiceImp;

    @Autowired
    FileStorageService fileStorageService = new FileStorageService();

    @Autowired
    UserFollowService userFollowService;

    @Autowired
    OrderServiceImp orderService;

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
    public QuickResponse<Map<String, Object>> getCurrentUser(HttpServletRequest request) {
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

        // ========== 统计数据 ==========
        int buyCount = orderServiceImp.countByBuyerId(userId);
        int sellCount = orderServiceImp.countBySellerId(userId);
        int favCount = favoriteServiceImp.countByUserId(userId);
        int fansCount = userFollowService.countFans(userId);
        int followCount = userFollowService.countFollows(userId);

        Map<String, Object> statistics = new HashMap<>();
        statistics.put("buyCount", buyCount);
        statistics.put("sellCount", sellCount);
        statistics.put("favCount", favCount);
        statistics.put("fans_count", fansCount);
        statistics.put("follow_count", followCount);

        Map<String, Object> data = new HashMap<>();
        data.put("user", user);
        data.put("statistics", statistics);

        return QuickResponse.success(data, "获取用户详情成功");
    }

    /**
     * 上传头像接口
     */
    @PostMapping("/upadavatar")
    public QuickResponse<String> upadavatar(@RequestBody MultipartFile file, @RequestHeader String token,HttpServletRequest request) throws IOException {
        if (StringUtils.isEmpty(token)) {
            return QuickResponse.fail(Const.FAIL, "未登录，请先登录");
        }
        Long userId = JwtUtil.getUserId(token);
        if (userId == null) {
            return QuickResponse.fail(Const.FAIL, "token无效或已过期");
        }
//        int userId = Integer.parseInt(token.split("_")[0]);
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

    @PostMapping("/register")
    public QuickResponse<User> register(@RequestBody User user) {
        if (StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword())) {
            return QuickResponse.fail(Const.EMPTY_PARAM, "账号或密码不能为空");
        }
        if (userServiceImp.register(user)) {
            return QuickResponse.success(user, "注册成功");
        } else {
            return QuickResponse.fail(Const.ERROR_PARAM, "注册失败");
        }
    }

    // 获取我买到的订单
    @GetMapping("/orders/buy")
    public QuickResponse<Map<String, Object>> getBuyOrders(
            @RequestParam(defaultValue = "all") String tab,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
//            @RequestHeader("X-User-Id") Long userId,
            HttpServletRequest request
    ) {
        String token = request.getHeader("Authorization");
        if (StringUtils.isEmpty(token)) {
            return QuickResponse.fail(Const.FAIL, "未登录，请先登录");
        }
        Long userId = JwtUtil.getUserId(token);

        IPage<Order> pageData = orderService.getBuyOrdersByTab(userId, tab, page, pageSize);
        Map<String, Object> result = new HashMap<>();
        result.put("records", pageData.getRecords());
        result.put("total", pageData.getTotal());
        return QuickResponse.success(result, "获取我买到的订单成功");
    }

    // 获取我卖出的订单
    // 卖家视角：获取我卖出的订单
    @GetMapping("/orders/sell")
    public QuickResponse<Map<String, Object>> getSellOrders(
            @RequestParam(defaultValue = "all") String tab,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
//            @RequestHeader("X-User-Id") Long userId
            HttpServletRequest request

    ) {
        String token = request.getHeader("Authorization");
        if (StringUtils.isEmpty(token)) {
            return QuickResponse.fail(Const.FAIL, "未登录，请先登录");
        }
        Long userId = JwtUtil.getUserId(token);
        IPage<Order> pageData = orderService.getSellOrdersByTab(userId, tab, page, pageSize);
        Map<String, Object> result = new HashMap<>();
        result.put("records", pageData.getRecords());
        result.put("total", pageData.getTotal());
        return QuickResponse.success(result, "获取我卖出的订单成功");
    }

    // 获取个人资料
//    @GetMapping("/profile")
//    public QuickResponse<?> getProfile(@RequestHeader("X-User-Id") Long userId) {
//        var profile = userServiceImp.getProfile(userId);
//        return QuickResponse.success(profile, "获取个人资料成功");
//    }

//    // 更新个人资料
//    @PostMapping("/profile")
//    public QuickResponse<?> updateProfile(@RequestHeader("X-User-Id") Long userId, @RequestBody Map<String, Object> profile) {
//        userService.updateProfile(userId, profile);
//        return QuickResponse.success("更新成功");
//    }

    // 收藏列表
//    @GetMapping("/favorites")
//    public QuickResponse<?> getFavorites(@RequestHeader("X-User-Id") Long userId) {
//        var favs = userService.getFavorites(userId);
//        return QuickResponse.success(favs, "获取收藏成功");
//    }

    // 账户安全信息
//    @GetMapping("/security")
//    public QuickResponse<?> getSecurity(@RequestHeader("X-User-Id") Long userId) {
//        var sec = userService.getSecurityInfo(userId);
//        return QuickResponse.success(sec, "获取账户安全信息成功");
//    }
}