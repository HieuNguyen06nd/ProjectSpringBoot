package com.hieunguyen.controller;

import com.hieunguyen.dto.response.ResponseData;
import com.hieunguyen.exception.ErrorCode;
import com.hieunguyen.model.Color;
import com.hieunguyen.service.ColorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/colors")
@RequiredArgsConstructor
public class ColorController {

    private final ColorService colorService;

    // Tạo màu mới
    @PostMapping
    public ResponseData<Color> createColor(@RequestBody Color color) {
        Color newColor = colorService.createColor(color);
        return new ResponseData<>(200, "Màu sắc được tạo thành công", newColor);
    }

    // Lấy màu theo ID
    @GetMapping("/{id}")
    public ResponseData<Color> getColorById(@PathVariable Long id) {
        Color color = colorService.getColorById(id);
        if (color == null) {
            return new ResponseData<>(ErrorCode.NOT_FOUND.getCode(), "Màu sắc không tồn tại", null);
        }
        return new ResponseData<>(200, "Màu sắc được tìm thấy", color);
    }

    // Lấy danh sách tất cả các màu sắc
    @GetMapping
    public ResponseData<List<Color>> getAllColors() {
        List<Color> colors = colorService.getAllColors();
        return new ResponseData<>(200, "Danh sách màu sắc", colors);
    }

    // Cập nhật màu sắc
    @PutMapping("/{id}")
    public ResponseData<Color> updateColor(@PathVariable Long id, @RequestBody Color color) {
        Color updatedColor = colorService.updateColor(id, color);
        if (updatedColor == null) {
            return new ResponseData<>(ErrorCode.NOT_FOUND.getCode(), "Màu sắc không tồn tại", null);
        }
        return new ResponseData<>(200, "Màu sắc được cập nhật thành công", updatedColor);
    }

    // Xóa màu sắc
    @DeleteMapping("/{id}")
    public ResponseData<Void> deleteColor(@PathVariable Long id) {
        colorService.deleteColor(id);
        return new ResponseData<>(200, "Màu sắc đã được xóa", null);
    }
}
