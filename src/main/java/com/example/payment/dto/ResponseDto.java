package com.example.payment.dto;

import org.springframework.http.HttpStatus;

public class ResponseDto {

    private HttpStatus statusCode;

    private String statusMsg;

    public ResponseDto(HttpStatus statusCode, String statusMsg) {
        this.statusCode = statusCode;
        this.statusMsg = statusMsg;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }
}
