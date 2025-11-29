package com.finance.exception;


public enum ErrorType {
    USER_NOT_FOUND(1001, "Lỗi: Không tìm thấy người dùng được yêu cầu."),
    INVALID_PASSWORD(1002, "Lỗi: Mật khẩu hiện tại không chính xác."),
    USERNAME_EXISTS(1003, "Lỗi: Tên người dùng đã tồn tại."),
    EMAIL_EXISTS(1004, "Lỗi: Email đã tồn tại."),
    AUTH_USER_NOT_FOUND(1005, "Lỗi: Không tìm thấy người dùng đã xác thực "),
    GENERIC_ERROR(5000, "Lỗi: Đã xảy ra lỗi không xác định.");

    private final int code;
    private final String message;

    ErrorType(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}