package com.hieunguyen.shopstorev2.exception;

public class ErrorCodes {

    // 0 ~ 999: Success
    public static final int SUCCESS = 0;

    // 1000 ~ 1999: Authentication errors
    public static final int UNAUTHORIZED = 1000;
    public static final int FORBIDDEN = 1001;
    public static final int INVALID_TOKEN = 1002;

    // 2000 ~ 2999: User related errors
    public static final int USER_NOT_FOUND = 2000;
    public static final int USER_ALREADY_EXISTS = 2001;
    public static final int INVALID_USER_DATA = 2002;

    // 3000 ~ 3999: Product related errors
    public static final int PRODUCT_NOT_FOUND = 3000;
    public static final int OUT_OF_STOCK = 3001;
    public static final int INVALID_PRODUCT_DATA = 3002;

    // 4000 ~ 4999: Order related errors
    public static final int ORDER_NOT_FOUND = 4000;
    public static final int INVALID_ORDER_STATUS = 4001;

    // 5000 ~ 5999: Server/internal errors
    public static final int INTERNAL_SERVER_ERROR = 5000;
    public static final int DATABASE_ERROR = 5001;

    // 6000 ~ 6999: Discount/voucher errors
    public static final int DISCOUNT_NOT_FOUND = 6000;
    public static final int DISCOUNT_EXPIRED = 6001;
    public static final int DISCOUNT_ALREADY_USED = 6002;

    // 7000 ~ 7999: Cart errors
    public static final int CART_ITEM_NOT_FOUND = 7000;
    public static final int CART_EMPTY = 7001;
}
