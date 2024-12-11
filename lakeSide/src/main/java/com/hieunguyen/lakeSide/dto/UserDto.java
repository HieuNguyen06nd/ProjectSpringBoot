package com.hieunguyen.lakeSide.dto;

import com.hieunguyen.lakeSide.enums.UserRole;
import lombok.Data;

@Data
public class UserDto {

    private Long id;

    private String email;

    private UserRole userRole;

    private  String name;
}
