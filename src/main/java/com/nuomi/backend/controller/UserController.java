package com.nuomi.backend.controller;

import com.alibaba.fastjson.JSONObject;
import com.nuomi.backend.model.QuickResponse;
import com.nuomi.backend.model.User;
import com.nuomi.backend.model.Const;
import com.nuomi.backend.service.imp.UserServiceImp;
import com.nuomi.backend.utils.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController("/api/user")
public class UserController {
    @Autowired
    UserServiceImp userServiceImp;
    QuickResponse quickResponse = new QuickResponse();
    FileStorageService fileStorageService = new FileStorageService();
    @PostMapping("/login")
    public JSONObject login(@RequestBody User loginuser) {
        //判空
        if (StringUtils.isEmpty(loginuser.getUsername()) || StringUtils.isEmpty(loginuser.getPassword())) {
            return quickResponse.getQuickResponse(Const.EMPTY_PARAM,"账号或密码不能为空");
        }
        User user = userServiceImp.login(loginuser.getUsername(),loginuser.getPassword());
        if (user == null) {
            return quickResponse.getQuickResponse(Const.ERROR_PARAM,"账号错误，或密码不正确");
        }else {
            return quickResponse.getQuickDataResponse(Const.SUCCESS,user,"登录成功");
        }
    }

    @RequestMapping(value = "upadavatar",method = RequestMethod.POST)
    public JSONObject upadavatar(@RequestBody MultipartFile file,@RequestHeader String token) throws IOException {
        //获取用户id
        int userId = Integer.parseInt(token.split("_")[0]);
        //获取文件名
        String fileName = file.getOriginalFilename();
        //获取文件后缀
        String fileSuffix = fileName.substring(fileName.lastIndexOf("."));
        //判断文件类型
        if (!fileSuffix.equals(".jpg") && !fileSuffix.equals(".png") && !fileSuffix.equals(".jpeg")) {
            return quickResponse.getQuickResponse(Const.ERROR_PARAM,"文件类型不正确");
        }
        //判断文件大小
        if (file.getSize() > 1024 * 1024 * 2) {
            return quickResponse.getQuickResponse(Const.ERROR_PARAM,"文件大小超过2MB");
        }
        //存储文件
        if (userServiceImp.upAdavatar(file,userId)) {
            return quickResponse.getQuickResponse(Const.SUCCESS,"头像上传成功");
        }else {
            return quickResponse.getQuickResponse(Const.ERROR_PARAM,"头像上传失败");
        }
    }

}
