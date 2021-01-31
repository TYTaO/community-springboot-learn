package com.tytao.community.exception;

public class CustomizeException extends RuntimeException {
    private String message;
    private Integer code;

    public CustomizeException(CustomizeErrorCode customizeErrorCode) {
        this.code = customizeErrorCode.getCode();
        this.message = customizeErrorCode.getMessage();
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    public Integer getCode(){
        return code;
    }
}
