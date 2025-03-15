package com.hieunguyen.identityservice.mapper;

import com.hieunguyen.identityservice.dto.request.UserCreationRequest;
import com.hieunguyen.identityservice.dto.request.UserUpdateRequest;
import com.hieunguyen.identityservice.dto.response.UserResponse;
import com.hieunguyen.identityservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
