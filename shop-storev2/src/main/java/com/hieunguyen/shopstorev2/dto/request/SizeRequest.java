package com.hieunguyen.shopstorev2.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SizeRequest {
    @NotBlank
    private String name;
}