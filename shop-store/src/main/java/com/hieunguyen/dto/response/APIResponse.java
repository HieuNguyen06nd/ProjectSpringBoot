package com.hieunguyen.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    /**
     * Mã phản hồi: 1000 nghĩa là thành công, các mã khác tương ứng với lỗi.
     */
    private int code;

    /**
     * Thông điệp phản hồi.
     */
    private String message;

    /**
     * Kết quả trả về, nếu có.
     */
    private T result;

    /**
     * Thời gian phản hồi.
     */
    private Instant timestamp = Instant.now();

    // Factory method cho thành công
    public static <T> ApiResponse<T> success(T result, String message) {
        return ApiResponse.<T>builder()
                .code(1000)  // 1000 là mã thành công
                .message(message)
                .result(result)
                .timestamp(Instant.now())
                .build();
    }

    // Factory method cho lỗi
    public static <T> ApiResponse<T> error(String message, int code) {
        return ApiResponse.<T>builder()
                .code(code)
                .message(message)
                .result(null)
                .timestamp(Instant.now())
                .build();
    }
}
