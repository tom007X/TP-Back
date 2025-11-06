package com.example.ShippingMicroservice.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ApiErrorResponse {

    private LocalDateTime fechaHora;
    private int status;
    private String error;
    private String message;

    public ApiErrorResponse(int status, String error,String msg){

        this.fechaHora = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = msg;

    }

}


