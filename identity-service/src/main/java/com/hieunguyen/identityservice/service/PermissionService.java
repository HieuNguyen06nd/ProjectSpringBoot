package com.hieunguyen.identityservice.service;

import com.hieunguyen.identityservice.dto.request.PermissionRequest;
import com.hieunguyen.identityservice.dto.response.PermissionResponse;

import java.util.List;

public interface PermissionService {

    PermissionResponse createPermission(PermissionRequest request);

    List<PermissionResponse> getAllPermissions();

    void deletePermission(String permission);
}
