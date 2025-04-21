package com.hieunguyen.controller;

import com.hieunguyen.dto.response.ResponseData;
import com.hieunguyen.exception.ErrorCode;
import com.hieunguyen.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileStorageService fileStorageService;
    private final String UPLOAD_DIR = "uploads/";

    @PostMapping("/upload")
    public ResponseData<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("module") String module) throws IOException {

        // Tạo thư mục nếu chưa tồn tại
        Path uploadPath = Paths.get(UPLOAD_DIR + module);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Tạo tên file unique
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Trả về đường dẫn đầy đủ
        String fileUrl = "http://localhost:8080/" + UPLOAD_DIR + module + "/" + filename;

        return new ResponseData<>(200, "Upload thành công", fileUrl);
    }

    @GetMapping("/{module}/{filename:.+}")
    public byte[] downloadFile(
            @PathVariable String module,
            @PathVariable String filename) {

        try {
            String fullPath = module + "/" + filename;
            return fileStorageService.loadFileAsBytes(fullPath);

        } catch (Exception e) {
            log.error("Download failed: {}", e.getMessage());
            throw new RuntimeException("File not found");
        }
    }

    @DeleteMapping("/{module}/{filename:.+}")
    public ResponseData<Void> deleteFile(
            @PathVariable String module,
            @PathVariable String filename) {

        try {
            String fullPath = module + "/" + filename;
            fileStorageService.deleteFile(fullPath);

            log.info("File deleted: {}", fullPath);
            return new ResponseData<>(200, "Xóa file thành công", null);

        } catch (Exception e) {
            log.error("Delete failed: {}", e.getMessage());
            return new ResponseData<>(ErrorCode.INTERNAL_SERVER_ERROR.getCode(),
                    "Xóa file thất bại: " + e.getMessage(), null);
        }
    }

    private String buildFileUrl(String filename) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/files/")
                .path(filename)
                .toUriString();
    }

    private boolean isValidModule(String module) {
        return module.matches("^[a-zA-Z0-9_-]+$");
    }
}