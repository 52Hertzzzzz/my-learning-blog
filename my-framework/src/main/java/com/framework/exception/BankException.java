package com.framework.exception;

public class BankException extends RuntimeException {

    private int code;

    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public BankException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

}
