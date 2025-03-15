package com.hieunguyen.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class VnPayService {

    @Value("${vnpay.tmnCode}")
    private String tmnCode;

    @Value("${vnpay.hashSecret}")
    private String hashSecret;

    @Value("${vnpay.payUrl}")
    private String payUrl;

    @Value("${vnpay.returnUrl}")
    private String returnUrl;

    public String createPaymentUrl(Long orderId, Double amount, String orderInfo, String bankCode) {
        // Khởi tạo tham số
        Map<String, String> params = new HashMap<>();
        params.put("vnp_Version", "2.1.0");
        params.put("vnp_Command", "pay");
        params.put("vnp_TmnCode", tmnCode);
        params.put("vnp_Amount", String.valueOf((long) (amount * 100)));
        params.put("vnp_CurrCode", "VND");
        params.put("vnp_TxnRef", String.valueOf(orderId));
        params.put("vnp_OrderInfo", orderInfo != null ? orderInfo : "Payment for order " + orderId);
        params.put("vnp_OrderType", "other");
        params.put("vnp_Locale", "vn");
        params.put("vnp_ReturnUrl", returnUrl);
        params.put("vnp_CreateDate", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));

        if (bankCode != null && !bankCode.isEmpty()) {
            params.put("vnp_BankCode", bankCode);
        }

        // Sắp xếp tham số theo key
        List<String> fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);

        // Tạo chuỗi hash
        StringBuilder hashData = new StringBuilder();
        for (String fieldName : fieldNames) {
            hashData.append(fieldName).append('=').append(params.get(fieldName)).append('&');
        }
        hashData.deleteCharAt(hashData.length() - 1); // Xóa dấu & cuối cùng

        // Tạo mã bảo mật (secure hash)
        String secureHash = hmacSHA512(hashSecret, hashData.toString());
        params.put("vnp_SecureHash", secureHash);

        // Tạo query string (không đưa vnp_SecureHash vào khi sắp xếp)
        StringBuilder query = new StringBuilder();
        for (String fieldName : fieldNames) {
            query.append(fieldName).append('=').append(urlEncode(params.get(fieldName))).append('&');
        }
        query.append("vnp_SecureHash=").append(secureHash);

        // Log để kiểm tra
        System.out.println("Raw params: " + params);
        System.out.println("Hash data: " + hashData);
        System.out.println("Query string: " + query);
        System.out.println("Secure hash: " + secureHash);
        System.out.println("Current server time: " + LocalDateTime.now());


        return payUrl + "?" + query;
    }



    private String urlEncode(String value) {
        if (value == null) {
            return ""; // Trả về chuỗi rỗng nếu value null
        }
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8)
                    .replace("+", "%20")
                    .replace("*", "%2A")
                    .replace("%7E", "~");
        } catch (Exception e) {
            throw new RuntimeException("Error while encoding URL: " + value, e);
        }
    }



    public String hmacSHA512(String secretKey, String data) {
        try {
            Mac hmac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac.init(secretKeySpec);
            byte[] hashBytes = hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hash = new StringBuilder();
            for (byte b : hashBytes) {
                hash.append(String.format("%02x", b));
            }
            return hash.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error while hashing data", e);
        }
    }


}
