package com.example.spring.aop;

public class StudentException extends Exception {

    private String mess;

    public String getMess() {
        return mess;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }
}
