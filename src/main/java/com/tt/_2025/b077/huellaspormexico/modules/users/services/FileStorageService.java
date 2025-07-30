package com.tt._2025.b077.huellaspormexico.modules.users.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageService {
    String saveProfilePicture(MultipartFile file, String username) throws IOException;
    boolean isValidImage(MultipartFile file);
    void deleteProfilePicture(String pictureUrl);
}