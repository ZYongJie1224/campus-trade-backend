package com.nuomi.backend.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.nuomi.backend.mapper.UserMapper;
import com.nuomi.backend.model.User;
import com.nuomi.backend.service.UserService;
import com.nuomi.backend.utils.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
@Service
public class UserServiceImp implements UserService {
    @Autowired
    UserMapper userMapper;
    FileStorageService fileStorageService;
    @Override
    public User login(String username, String password) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        List<User> users = userMapper.selectList(queryWrapper);
        if (!users.isEmpty() && users.get(0).getPassword().equals(password)) {
            return users.get(0);
        }
        return null;
    }


    @Override
    public boolean upAdavatar(MultipartFile file, int userId) throws IOException {
        String fileName = userId + "_avatar" + fileStorageService.getFileExtension(file.getOriginalFilename());
        //更新数据库adavtarurl字段
        int col = userMapper.update(null, new UpdateWrapper<User>().eq("user_id", userId).set("avatar", fileName).set("update_time", new Date())); // 同时更新修改时间
        boolean flag = fileStorageService.storeAdavtar(file,userId);
        if (flag && col > 0) {
            return true;
        }else{
            return false;
        }
    }
    @Override
    public User getById(Long userId) {
        return userMapper.selectById(userId);
    }
}
