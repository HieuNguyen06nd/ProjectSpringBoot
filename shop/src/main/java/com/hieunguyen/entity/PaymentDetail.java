package com.hieunguyen.entity;

import com.hieunguyen.utils.PaymentStatus;
import lombok.Data;

@Data
public class PaymentDetail {

    private String paymentId;

    private String razorpayPaymentLinkId;

    private String razorpayPaymentLinkReferenceId;

    private String  razorpayPaymentLinkStatus;

    private String  razorpayPaymentIdZWSP;

    private PaymentStatus status;
}
