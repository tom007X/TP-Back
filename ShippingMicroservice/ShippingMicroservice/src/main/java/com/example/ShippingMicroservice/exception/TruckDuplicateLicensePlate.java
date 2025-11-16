package com.example.ShippingMicroservice.exception;

public class TruckDuplicateLicensePlate extends RuntimeException {
    public TruckDuplicateLicensePlate(String licensePlate) {
        super("The truck with license plate " + licensePlate + " exists");
    }
}
