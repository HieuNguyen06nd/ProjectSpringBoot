package com.hieunguyen.utils;

public enum PaymentStatus {
    PENDING,    // Chờ thanh toán
    SUCCESS,    // Thanh toán thành công
    FAILED,     // Thanh toán thất bại
    CONFIRMED   // Xác nhận thanh toán (dành cho VNPAY, MoMo)
}
