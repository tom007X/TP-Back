package com.example.TruckMicroservice.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message){
        super(message);
    }

}
