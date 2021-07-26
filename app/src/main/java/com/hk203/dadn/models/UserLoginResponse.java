package com.hk203.dadn.models;

public class UserLoginResponse {
    private String message;
    private int code;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
