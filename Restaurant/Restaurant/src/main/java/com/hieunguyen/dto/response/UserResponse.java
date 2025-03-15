package com.hieunguyen.dto.response;

import com.hieunguyen.entity.User;
import com.hieunguyen.enums.Role;
import com.hieunguyen.enums.StatusUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String name;
    private Role role;
    private String email;
    private String phone;
    private String salary;
    private Date hireDate;
    private StatusUser status;

}
