package com.hieunguyen.utils;

import java.time.LocalDateTime;

public enum DiscountStatus {
    UPCOMING,    // Sắp diễn ra
    ACTIVE,      // Đang hoạt động
    EXPIRED ;     // Đã kết thúc

    public static DiscountStatus fromDates(LocalDateTime validFrom, LocalDateTime validTo) {
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(validFrom)) {
            return UPCOMING;
        } else if (now.isAfter(validTo)) {
            return EXPIRED;
        } else {
            return ACTIVE;
        }
    }
}
