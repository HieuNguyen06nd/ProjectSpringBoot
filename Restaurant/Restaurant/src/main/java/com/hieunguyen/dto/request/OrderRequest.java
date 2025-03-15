package com.hieunguyen.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class OrderRequest {

    // Nếu không truyền orderTime thì service có thể đặt mặc định là LocalDateTime.now()
    private LocalDateTime orderTime;

    @NotNull(message = "Table ID is required")
    private Long tableId;
}
