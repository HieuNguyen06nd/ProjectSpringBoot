package com.hieunguyen.shopstorev2.exception;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {
    private final int code; // Mã lỗi tự định nghĩa

    public AppException(int code, String message) {
        super(message);
        this.code = code;
    }
}
