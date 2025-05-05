package com.hieunguyen.shopstorev2.controller;

import com.hieunguyen.shopstorev2.dto.request.SizeRequest;
import com.hieunguyen.shopstorev2.dto.response.ApiResponse;
import com.hieunguyen.shopstorev2.dto.response.SizeResponse;
import com.hieunguyen.shopstorev2.service.SizeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sizes")
@RequiredArgsConstructor
public class SizeController {

    private final SizeService sizeService;

    @PostMapping
    public ApiResponse<SizeResponse> create(@RequestBody @Valid SizeRequest request) {
        return ApiResponse.success(sizeService.create(request));
    }

    @GetMapping
    public ApiResponse<List<SizeResponse>> getAll() {
        return ApiResponse.success(sizeService.getAll());
    }

    @PutMapping("/{id}")
    public ApiResponse<SizeResponse> update(@PathVariable Long id, @RequestBody @Valid SizeRequest request) {
        return ApiResponse.success(sizeService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(@PathVariable Long id) {
        sizeService.delete(id);
        return ApiResponse.success("Xóa kích thước thành công");
    }
}
