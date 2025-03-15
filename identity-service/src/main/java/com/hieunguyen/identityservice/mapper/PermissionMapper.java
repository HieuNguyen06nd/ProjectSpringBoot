package com.hieunguyen.identityservice.mapper;

import com.hieunguyen.identityservice.dto.request.PermissionRequest;
import com.hieunguyen.identityservice.dto.response.PermissionResponse;
import com.hieunguyen.identityservice.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
