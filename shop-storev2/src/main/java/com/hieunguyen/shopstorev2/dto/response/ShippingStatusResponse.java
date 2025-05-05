package com.hieunguyen.shopstorev2.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ShippingStatusResponse {
    private String trackingCode;
    private String status;
    private LocalDateTime updatedAt;
}

