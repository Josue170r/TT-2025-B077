package com.tt._2025.b077.huellaspormexico.modules.users.services.impl;

import com.tt._2025.b077.huellaspormexico.modules.users.services.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@Profile("dev")
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${app.file.upload-dir}")
    private String uploadDir;

    @Value("${app.file.base-url}")
    private String baseUrl;

    /**
     * Saves uploaded file and returns the public URL
     */
    @Override
    public String saveProfilePicture(MultipartFile file, String username) throws IOException {
        if (!isValidImage(file)) {
            throw new IllegalArgumentException("El archivo no es válido");
        }

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFilename = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFilename);
        String uniqueFilename = username + "_" + UUID.randomUUID() + fileExtension;

        Path filePath = uploadPath.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return baseUrl + "/profile-pictures/" + uniqueFilename;
    }

    /**
     * Validates if the uploaded file is a valid image
     */
    @Override
    public boolean isValidImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        if (file.getSize() > 10 * 1024 * 1024) {
            return false;
        }

        String contentType = file.getContentType();
        return contentType != null && (
                contentType.equals("image/jpeg") ||
                        contentType.equals("image/jpg") ||
                        contentType.equals("image/png") ||
                        contentType.equals("image/gif") ||
                        contentType.equals("image/webp")
        );
    }

    /**
     * Gets file extension from filename
     */
    private String getFileExtension(String filename) {
        if (filename != null && filename.contains(".")) {
            return filename.substring(filename.lastIndexOf(".")).toLowerCase();
        }
        return ".jpg";
    }

    /**
     * Deletes a profile picture file
     */
    @Override
    public void deleteProfilePicture(String pictureUrl) {
        try {
            if (pictureUrl != null && pictureUrl.startsWith(baseUrl)) {
                String filename = pictureUrl.substring(pictureUrl.lastIndexOf("/") + 1);
                Path filePath = Paths.get(uploadDir).resolve(filename);
                Files.deleteIfExists(filePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar el imagen de perfil");
        }
    }
}
