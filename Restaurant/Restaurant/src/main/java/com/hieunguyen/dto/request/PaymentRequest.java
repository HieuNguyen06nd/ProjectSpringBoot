package com.hieunguyen.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {
    private Long orderId;
    private String orderInfo;
    private Double amount;
    private String bankCode;
}
