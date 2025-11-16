package com.example.ShippingMicroservice.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String className, Long id) {
        super(className + " with Id " + id + " not found");
    }

        public NotFoundException(String message) {
        super(message);
    }
}
