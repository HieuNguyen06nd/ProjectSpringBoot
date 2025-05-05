package com.hieunguyen.shopstorev2.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {
    private String username;
    private String phone;
    private String email;
    private String avatarUrl;
}
