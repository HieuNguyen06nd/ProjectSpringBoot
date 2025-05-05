package com.hieunguyen.shopstorev2.service.impl;

import com.hieunguyen.shopstorev2.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${upload.path:uploads}")
    private String uploadRoot;

    @Override
    public String saveFile(MultipartFile file, String subDirectory) {
        try {
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
            String filename = UUID.randomUUID() + extension;

            Path uploadDir = Paths.get(uploadRoot).toAbsolutePath().normalize();
            Path fullDir = uploadDir.resolve(subDirectory);
            Files.createDirectories(fullDir); // ✅ Tạo thư mục nếu chưa tồn tại

            Path filePath = fullDir.resolve(filename);
            file.transferTo(filePath.toFile()); // ✅ Lưu file

            return "/" + uploadRoot + "/" + subDirectory + "/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Lưu file thất bại", e);
        }
    }


}