package com.hieunguyen.controller;

import com.hieunguyen.dto.request.PaymentRequest;
import com.hieunguyen.dto.response.PaymentResponse;
import com.hieunguyen.service.VnPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final VnPayService vnPayService;

    @PostMapping("/create")
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody PaymentRequest request) {
        String paymentUrl = vnPayService.createPaymentUrl(
                request.getOrderId(),
                request.getAmount(),
                request.getOrderInfo(),
                request.getBankCode()
        );

        return ResponseEntity.ok(
                PaymentResponse.builder()
                        .status("SUCCESS")
                        .message("Payment URL created successfully")
                        .paymentUrl(paymentUrl)
                        .build()
        );
    }
    @GetMapping("/vnpay-callback")
    public ResponseEntity<String> vnpayCallback(@RequestParam Map<String, String> params) {
        String responseCode = params.get("vnp_ResponseCode");

        if ("00".equals(responseCode)) {
            return ResponseEntity.ok("Payment successful!");
        } else {
            return ResponseEntity.status(400).body("Payment failed!");
        }
    }

}
