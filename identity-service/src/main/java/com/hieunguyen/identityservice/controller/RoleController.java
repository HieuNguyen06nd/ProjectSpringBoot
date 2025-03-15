package com.hieunguyen.identityservice.controller;

import com.hieunguyen.identityservice.dto.request.ApiResponse;
import com.hieunguyen.identityservice.dto.request.PermissionRequest;
import com.hieunguyen.identityservice.dto.request.RoleRequest;
import com.hieunguyen.identityservice.dto.response.PermissionResponse;
import com.hieunguyen.identityservice.dto.response.RoleResponse;
import com.hieunguyen.identityservice.service.PermissionService;
import com.hieunguyen.identityservice.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {

    RoleService roleService;

    @PostMapping("")
    public ApiResponse<RoleResponse> create(@RequestBody RoleRequest request) {

        return ApiResponse.<RoleResponse>builder()
                .result( roleService.create(request))
                .build();
    }

    @GetMapping("")
    public ApiResponse<List<RoleResponse>> getAllRoles() {
        return ApiResponse.<List<RoleResponse>>builder()
                .result( roleService.getAllRoles())
                .build();
    }

    @DeleteMapping("/{role}")
    public ApiResponse<Void> deleteRole(@PathVariable String role) {
        roleService.deleteRole(role);
        return ApiResponse.<Void>builder().build();
    }
}
