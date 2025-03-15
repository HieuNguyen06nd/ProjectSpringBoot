package com.hieunguyen.identityservice.mapper;

import com.hieunguyen.identityservice.dto.request.PermissionRequest;
import com.hieunguyen.identityservice.dto.request.RoleRequest;
import com.hieunguyen.identityservice.dto.response.PermissionResponse;
import com.hieunguyen.identityservice.dto.response.RoleResponse;
import com.hieunguyen.identityservice.entity.Permission;
import com.hieunguyen.identityservice.entity.Role;
import org.mapstruct.MapMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
