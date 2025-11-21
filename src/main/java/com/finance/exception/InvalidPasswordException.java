package com.finance.exception;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException(String message) {
        super(message);
    }

    public InvalidPasswordException(String message, Throwable cause) {
        super(message, cause);
    }
    public static InvalidPasswordException incorrectCurrentPassword() {
        return new InvalidPasswordException("Current password is incorrect");
    }
}
