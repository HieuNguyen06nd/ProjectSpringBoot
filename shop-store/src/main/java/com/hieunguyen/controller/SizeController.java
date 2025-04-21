package com.hieunguyen.controller;

import com.hieunguyen.dto.response.ResponseData;
import com.hieunguyen.exception.ErrorCode;
import com.hieunguyen.model.Size;
import com.hieunguyen.service.SizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sizes")
@RequiredArgsConstructor
public class SizeController {

    private final SizeService sizeService;

    // Tạo size mới
    @PostMapping
    public ResponseData<Size> createSize(@RequestBody Size size) {
        Size newSize = sizeService.createSize(size);
        return new ResponseData<>(200, "Size được tạo thành công", newSize);
    }

    // Lấy size theo ID
    @GetMapping("/{id}")
    public ResponseData<Size> getSizeById(@PathVariable Long id) {
        Size size = sizeService.getSizeById(id);
        if (size == null) {
            return new ResponseData<>(ErrorCode.NOT_FOUND.getCode(), "Size không tồn tại", null);
        }
        return new ResponseData<>(200, "Size được tìm thấy", size);
    }

    // Lấy danh sách tất cả sizes
    @GetMapping
    public ResponseData<List<Size>> getAllSizes() {
        List<Size> sizes = sizeService.getAllSizes();
        return new ResponseData<>(200, "Danh sách size", sizes);
    }

    // Cập nhật size
    @PutMapping("/{id}")
    public ResponseData<Size> updateSize(@PathVariable Long id, @RequestBody Size size) {
        Size updatedSize = sizeService.updateSize(id, size);
        if (updatedSize == null) {
            return new ResponseData<>(ErrorCode.NOT_FOUND.getCode(), "Size không tồn tại", null);
        }
        return new ResponseData<>(200, "Size được cập nhật thành công", updatedSize);
    }

    // Xóa size
    @DeleteMapping("/{id}")
    public ResponseData<Void> deleteSize(@PathVariable Long id) {
        sizeService.deleteSize(id);
        return new ResponseData<>(200, "Size đã được xóa", null);
    }
}
