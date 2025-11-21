package com.finance.exception;


public enum ErrorType {
    USER_NOT_FOUND(1001, "Error: The requested user was not found."),
    INVALID_PASSWORD(1002, "Error: The current password is incorrect."),
    USERNAME_EXISTS(1003, "Error: Username already exists."),
    EMAIL_EXISTS(1004, "Error: Email already exists."),
    AUTH_USER_NOT_FOUND(1005, "Error: Authenticated user not found "),
    GENERIC_ERROR(5000, "Error: An unknown error occurred.");

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