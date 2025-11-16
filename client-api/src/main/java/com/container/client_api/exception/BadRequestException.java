package com.container.client_api.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message){
        super(message);
    }

}
