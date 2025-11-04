package com.example.DemoTruck.exception;

public class TruckNotFoundException extends RuntimeException{
    public TruckNotFoundException(Long id){
        super("Truck with Id is " + id + " not found" );
    }
}
