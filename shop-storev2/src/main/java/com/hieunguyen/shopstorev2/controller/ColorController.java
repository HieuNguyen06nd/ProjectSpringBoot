package com.hieunguyen.shopstorev2.controller;

import com.hieunguyen.shopstorev2.dto.request.ColorRequest;
import com.hieunguyen.shopstorev2.dto.response.ApiResponse;
import com.hieunguyen.shopstorev2.dto.response.ColorResponse;
import com.hieunguyen.shopstorev2.service.ColorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/colors")
@RequiredArgsConstructor
public class ColorController {

    private final ColorService colorService;

    @PostMapping
    public ApiResponse<ColorResponse> create(@RequestBody @Valid ColorRequest request) {
        return ApiResponse.success(colorService.create(request));
    }

    @GetMapping
    public ApiResponse<List<ColorResponse>> getAll() {
        return ApiResponse.success(colorService.getAll());
    }

    @PutMapping("/{id}")
    public ApiResponse<ColorResponse> update(@PathVariable Long id, @RequestBody @Valid ColorRequest request) {
        return ApiResponse.success(colorService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(@PathVariable Long id) {
        colorService.delete(id);
        return ApiResponse.success("Xóa màu thành công");
    }
}