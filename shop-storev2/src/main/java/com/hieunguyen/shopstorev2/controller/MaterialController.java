package com.hieunguyen.shopstorev2.controller;

import com.hieunguyen.shopstorev2.dto.request.MaterialRequest;
import com.hieunguyen.shopstorev2.dto.response.ApiResponse;
import com.hieunguyen.shopstorev2.dto.response.MaterialResponse;
import com.hieunguyen.shopstorev2.service.MaterialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materials")
@RequiredArgsConstructor
public class MaterialController {

    private final MaterialService materialService;

    @PostMapping
    public ApiResponse<MaterialResponse> create(@RequestBody @Valid MaterialRequest request) {
        return ApiResponse.success(materialService.create(request));
    }

    @GetMapping
    public ApiResponse<List<MaterialResponse>> getAll() {
        return ApiResponse.success(materialService.getAll());
    }

    @PutMapping("/{id}")
    public ApiResponse<MaterialResponse> update(@PathVariable Long id, @RequestBody @Valid MaterialRequest request) {
        return ApiResponse.success(materialService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(@PathVariable Long id) {
        materialService.delete(id);
        return ApiResponse.success("Xóa chất liệu thành công");
    }
}