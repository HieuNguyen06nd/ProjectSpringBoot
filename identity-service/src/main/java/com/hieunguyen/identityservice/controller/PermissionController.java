package com.hieunguyen.identityservice.controller;

import com.hieunguyen.identityservice.dto.request.ApiResponse;
import com.hieunguyen.identityservice.dto.request.PermissionRequest;
import com.hieunguyen.identityservice.dto.response.PermissionResponse;
import com.hieunguyen.identityservice.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {

    PermissionService permissionService;

    @PostMapping("")
    public ApiResponse<PermissionResponse> createPermission(@RequestBody PermissionRequest request) {

        return ApiResponse.<PermissionResponse>builder()
                .result( permissionService.createPermission(request))
                .build();
    }

    @GetMapping("")
    public ApiResponse<List<PermissionResponse>> getPermissions() {
        return ApiResponse.<List<PermissionResponse>>builder()
                .result(permissionService.getAllPermissions())
                .build();
    }

    @DeleteMapping("/{permission}")
    public ApiResponse<Void> deletePermission(@PathVariable String permission) {
        permissionService.deletePermission(permission);
        return ApiResponse.<Void>builder().build();
    }
}
