package com.hieunguyen.shopstorev2.exception;

import com.hieunguyen.shopstorev2.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ApiResponse<?> handleAppException(AppException ex, HttpServletResponse response) throws IOException {
        response.setStatus(200); // luôn trả HTTP 200
        return ApiResponse.error(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handleGenericException(Exception ex, HttpServletResponse response) throws IOException {
        ex.printStackTrace(); // log ra server console
        response.setStatus(200); // luôn trả HTTP 200
        return ApiResponse.error(5000, "Internal Server Error"); // code 5000 là lỗi hệ thống
    }
}
