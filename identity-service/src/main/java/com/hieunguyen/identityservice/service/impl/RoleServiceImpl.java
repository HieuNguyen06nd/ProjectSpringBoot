package com.hieunguyen.identityservice.service.impl;

import com.hieunguyen.identityservice.dto.request.RoleRequest;
import com.hieunguyen.identityservice.dto.response.RoleResponse;
import com.hieunguyen.identityservice.mapper.RoleMapper;
import com.hieunguyen.identityservice.repository.PermissionRepository;
import com.hieunguyen.identityservice.repository.RoleRepository;
import com.hieunguyen.identityservice.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImpl implements RoleService {
    RoleRepository roleRepository;
    RoleMapper roleMapper;
    PermissionRepository permissionRepository;


    @Override
    public RoleResponse create(RoleRequest request) {
        var role = roleMapper.toRole(request);
        var permissions = permissionRepository.findAllById(request.getPermissions());

        role.setPermissions(new HashSet<>(permissions));
        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    @Override
    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).collect(Collectors.toList());
    }

    @Override
    public void deleteRole(String role) {
        roleRepository.deleteById(role);
    }
}
