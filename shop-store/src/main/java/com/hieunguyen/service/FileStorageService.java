package com.hieunguyen.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

public interface FileStorageService {
    String storeFile(MultipartFile file, String module) throws IOException;
    byte[] loadFileAsBytes(String filePath) throws IOException;
    void deleteFile(String filePath) throws IOException;


    void deleteImages(Set<String> imageUrls);
    // Thêm phương thức mới
    String getFileUrl(String filename, String module);
}
