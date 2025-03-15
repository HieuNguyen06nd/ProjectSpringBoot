package com.hieunguyen.identityservice.service.impl;

import com.hieunguyen.identityservice.dto.request.PermissionRequest;
import com.hieunguyen.identityservice.dto.response.PermissionResponse;
import com.hieunguyen.identityservice.entity.Permission;
import com.hieunguyen.identityservice.mapper.PermissionMapper;
import com.hieunguyen.identityservice.repository.PermissionRepository;
import com.hieunguyen.identityservice.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionServiceImpl implements PermissionService {

    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    @Override
    public PermissionResponse createPermission(PermissionRequest request) {
       Permission permission = permissionMapper.toPermission(request);

       permissionRepository.save(permission);

       return permissionMapper.toPermissionResponse(permission);
    }

    @Override
    public List<PermissionResponse> getAllPermissions() {
        var permissions = permissionRepository.findAll();

       return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    @Override
    public void deletePermission(String permission) {
        permissionRepository.deleteById(permission);
    }


}
