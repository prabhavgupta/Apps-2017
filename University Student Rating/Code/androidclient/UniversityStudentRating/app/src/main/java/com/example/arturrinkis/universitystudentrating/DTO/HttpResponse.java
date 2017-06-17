package com.example.arturrinkis.universitystudentrating.DTO;


public class HttpResponse {
    private int code;
    private String message;

    public HttpResponse(){

    }
    public HttpResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessages(String message) {
        this.message = message != null ? message : "";
    }
}
