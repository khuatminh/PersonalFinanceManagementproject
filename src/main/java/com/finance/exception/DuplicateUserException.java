package com.finance.exception;

public class DuplicateUserException extends RuntimeException{
    public DuplicateUserException(String message){
        super(message);
    }
    public DuplicateUserException(String message,Throwable cause){
        super(message,cause);
    }

    public static DuplicateUserException forUserName(String userName){
        return new DuplicateUserException("Username already exists: " + userName);
    }

    public static DuplicateUserException forEmail(String userEmail){
        return new DuplicateUserException("UserEmail already exists: " + userEmail);
    }
}
