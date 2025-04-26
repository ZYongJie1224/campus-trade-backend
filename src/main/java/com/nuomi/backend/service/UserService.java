package com.nuomi.backend.service;

import com.nuomi.backend.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
    public User login(String username,String password);
    public boolean upAdavatar(MultipartFile file, int userId) throws IOException;
}
