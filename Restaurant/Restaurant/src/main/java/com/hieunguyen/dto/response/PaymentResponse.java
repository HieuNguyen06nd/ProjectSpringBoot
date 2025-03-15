package com.hieunguyen.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {
    private String status;
    private String message;
    private String paymentUrl;
}
