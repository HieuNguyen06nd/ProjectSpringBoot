package com.hieunguyen.dto.request;

import com.hieunguyen.enums.Role;
import com.hieunguyen.enums.StatusUser;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.util.Date;

@Getter
@Setter
public class UserCreateRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be 10 digits")
    private String phone;

    @Positive(message = "Salary must be a positive number")
    private String salary;

    private Role role;

}
