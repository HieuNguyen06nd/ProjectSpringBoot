package com.hieunguyen.shopstorev2.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressResponse {
    private Long id;
    private String fullName;
    private String phone;
    private String detail;
    private String city;
    private String district;
    private String ward;
    private Boolean isDefault;
}
