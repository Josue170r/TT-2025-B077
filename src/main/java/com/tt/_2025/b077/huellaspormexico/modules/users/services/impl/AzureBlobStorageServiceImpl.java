package com.tt._2025.b077.huellaspormexico.modules.users.services.impl;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.tt._2025.b077.huellaspormexico.modules.users.services.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@Profile("prod")
public class AzureBlobStorageServiceImpl implements FileStorageService {

    private final BlobContainerClient containerClient;

    public AzureBlobStorageServiceImpl(
            @Value("${azure.storage.connection-string}") String connectionString,
            @Value("${azure.storage.container-name}") String containerName) {

        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();

        this.containerClient = blobServiceClient.getBlobContainerClient(containerName);

        if (!this.containerClient.exists()) {
            this.containerClient.create();
        }
    }

    @Override
    public String saveProfilePicture(MultipartFile file, String username) throws IOException {
        if (!isValidImage(file)) {
            throw new IllegalArgumentException("El archivo no es válido");
        }

        String originalFilename = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFilename);
        String uniqueFilename = username + "_" + UUID.randomUUID() + fileExtension;

        BlobClient blobClient = containerClient.getBlobClient(uniqueFilename);

        BlobHttpHeaders headers = new BlobHttpHeaders()
                .setContentType(file.getContentType());

        blobClient.upload(file.getInputStream(), file.getSize(), true);
        blobClient.setHttpHeaders(headers);

        return blobClient.getBlobUrl();
    }

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

    private String getFileExtension(String filename) {
        if (filename != null && filename.contains(".")) {
            return filename.substring(filename.lastIndexOf(".")).toLowerCase();
        }
        return ".jpg";
    }

    @Override
    public void deleteProfilePicture(String pictureUrl) {
        try {
            if (pictureUrl != null && pictureUrl.contains(containerClient.getBlobContainerName())) {
                String filename = pictureUrl.substring(pictureUrl.lastIndexOf("/") + 1);
                BlobClient blobClient = containerClient.getBlobClient(filename);
                blobClient.deleteIfExists();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar la imagen de perfil");
        }
    }
}