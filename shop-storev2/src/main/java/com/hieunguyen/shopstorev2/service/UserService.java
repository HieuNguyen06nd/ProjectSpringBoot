package com.hieunguyen.shopstorev2.service;


import com.hieunguyen.shopstorev2.dto.request.ChangePasswordRequest;
import com.hieunguyen.shopstorev2.dto.request.UpdateUserRequest;
import com.hieunguyen.shopstorev2.dto.response.UserResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    UserResponse getCurrentUser();

    UserResponse updateCurrentUser(UpdateUserRequest request);

    void changePassword(ChangePasswordRequest request);

    UserResponse updateAvatar(MultipartFile file);

}
