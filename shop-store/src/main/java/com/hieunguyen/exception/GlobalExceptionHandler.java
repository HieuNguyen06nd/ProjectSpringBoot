package com.hieunguyen.exception;

import com.hieunguyen.dto.response.APIException;
import com.hieunguyen.dto.response.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Xử lý lỗi nghiệp vụ do bạn tự ném
    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIResponse<?>> handleAPIException(APIException ex) {
        APIResponse<?> response = new APIResponse<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Xử lý lỗi xác thực các tham số đầu vào (DTO)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse<?>> handleValidationException(MethodArgumentNotValidException ex) {
        StringBuilder errors = new StringBuilder();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.append(error.getField())
                    .append(": ")
                    .append(error.getDefaultMessage())
                    .append("; ");
        }
        APIResponse<?> response = new APIResponse<>(false, errors.toString(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Xử lý lỗi thiếu tham số trong request
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<APIResponse<?>> handleMissingParams(MissingServletRequestParameterException ex) {
        String name = ex.getParameterName();
        String message = "Thiếu tham số: " + name;
        APIResponse<?> response = new APIResponse<>(false, message, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Xử lý lỗi do tham số không hợp lệ
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<APIResponse<?>> handleIllegalArgumentException(IllegalArgumentException ex) {
        APIResponse<?> response = new APIResponse<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Xử lý các lỗi chưa được xử lý khác
    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse<?>> handleException(Exception ex) {
        APIResponse<?> response = new APIResponse<>(false, "Đã xảy ra lỗi: " + ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}