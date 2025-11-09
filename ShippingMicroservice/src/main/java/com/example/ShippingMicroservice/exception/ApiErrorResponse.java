package com.example.ShippingMicroservice.exception;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiErrorResponse {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;

    public ApiErrorResponse(int status, String error, String msg){

        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = msg;

    }

}


