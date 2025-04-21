package com.hieunguyen.service.impl;

import com.hieunguyen.service.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.*;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${app.file-upload-dir}")
    private String uploadDir;

    @Override
    public String storeFile(MultipartFile file, String module) throws IOException {
        // Validate
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File không được để trống");
        }
        if (!isValidModule(module)) {
            throw new IllegalArgumentException("Module không hợp lệ");
        }

        // Prepare filename
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : "";
        String filename = UUID.randomUUID() + fileExtension;
        String fullPath = module + "/" + filename;

        // Prepare directory
        Path modulePath = Paths.get(uploadDir, module).toAbsolutePath().normalize();
        Files.createDirectories(modulePath);

        // Save file
        Path targetLocation = modulePath.resolve(filename);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        log.info("File saved to: {}", targetLocation);
        return fullPath;
    }

    @Override
    public byte[] loadFileAsBytes(String filePath) throws IOException {
        Path normalizedPath = validateAndNormalizePath(filePath);
        return Files.readAllBytes(normalizedPath);
    }

    @Override
    public void deleteFile(String filePath) throws IOException {
        Path normalizedPath = validateAndNormalizePath(filePath);
        Files.deleteIfExists(normalizedPath);
        log.info("Deleted file: {}", normalizedPath);
    }

    @Override
    public String getFileUrl(String filename, String module) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/uploads/")
                .path(module + "/")
                .path(filename)
                .toUriString();
    }

    @Override
    public void deleteImages(Set<String> imageUrls) {
        imageUrls.forEach(url -> {
            try {
                // Trích xuất filename từ URL (bao gồm cả module)
                String filePath = url.replaceFirst(".*/uploads/", ""); // Lấy phần sau "/uploads/"

                // Tạo đường dẫn đầy đủ từ thư mục upload root
                Path fullPath = Paths.get(uploadDir, filePath).normalize().toAbsolutePath();

                // Kiểm tra bảo mật - đảm bảo đường dẫn nằm trong thư mục upload
                if (!fullPath.startsWith(Paths.get(uploadDir).normalize().toAbsolutePath())) {
                    throw new SecurityException("Attempt to delete file outside upload directory");
                }

                // Xóa file
                Files.deleteIfExists(fullPath);
                log.info("Deleted image: {}", fullPath);

            } catch (Exception e) {
                throw new RuntimeException("Failed to delete image: " + url, e);
            }
        });
    }

    // Helper methods
    private Path validateAndNormalizePath(String filePath) throws IOException {
        Path path = Paths.get(uploadDir, filePath).normalize();
        Path absolutePath = path.toAbsolutePath();

        if (!absolutePath.startsWith(Paths.get(uploadDir).toAbsolutePath())) {
            throw new SecurityException("Truy cập file ngoài thư mục cho phép");
        }
        if (!Files.exists(absolutePath)) {
            throw new IOException("File không tồn tại");
        }
        return absolutePath;
    }

    private boolean isValidModule(String module) {
        return module != null && module.matches("^[a-zA-Z0-9_-]+$");
    }
}