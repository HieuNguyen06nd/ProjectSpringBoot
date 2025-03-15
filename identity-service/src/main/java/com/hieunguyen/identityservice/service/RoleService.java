package com.hieunguyen.identityservice.service;

import com.hieunguyen.identityservice.dto.request.PermissionRequest;
import com.hieunguyen.identityservice.dto.request.RoleRequest;
import com.hieunguyen.identityservice.dto.response.PermissionResponse;
import com.hieunguyen.identityservice.dto.response.RoleResponse;

import java.util.List;

public interface RoleService {

    RoleResponse create(RoleRequest request);

    List<RoleResponse> getAllRoles();

    void deleteRole(String role);
}
