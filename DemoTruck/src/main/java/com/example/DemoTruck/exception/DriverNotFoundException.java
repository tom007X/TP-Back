package com.example.DemoTruck.exception;

import org.springframework.http.ResponseEntity;

public class DriverNotFoundException extends RuntimeException{
    public DriverNotFoundException(Long id){
        super("The driver with Id is " + id + " not found");
    }

}
