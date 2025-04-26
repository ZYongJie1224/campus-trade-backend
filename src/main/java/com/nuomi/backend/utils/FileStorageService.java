package com.nuomi.backend.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;

@Service
public class FileStorageService {

    @Value("${custom.avatar.upload-dir}")
    private String uploadDir;

    // 存储文件并返回访问路径
    public boolean storeAdavtar(MultipartFile file, int userId) throws IOException {
        // 确保目录存在
        Path dir = Paths.get(uploadDir);
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }
        // 生成唯一文件名（如：user123_avatar.jpg）
        String fileName = userId + "_avatar" + getFileExtension(file.getOriginalFilename());
            Path targetLocation = dir.resolve(fileName);
            // 保存文件（覆盖已存在的文件）
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return true;
    }

    // 获取文件扩展名
    public String getFileExtension(String fileName) {
        return fileName != null ? fileName.substring(fileName.lastIndexOf(".")) : "";
    }
}