package com.example.ShippingMicroservice.controller;

import com.example.ShippingMicroservice.model.Driver;
import com.example.ShippingMicroservice.service.DriverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService service;

    @GetMapping("/{id}")
    public ResponseEntity<Driver> getDriverById(@PathVariable Long id){
        Driver driver = service.findById(id);
        if (driver == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(driver);
    }

    @GetMapping
    public ResponseEntity<List<Driver>> getAllTruck(){
        List<Driver> drivers = service.findAll();
        if (drivers.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(drivers);
    }

    @PostMapping
    public ResponseEntity<?> createTruck(@Valid @RequestBody Driver driver){
        Driver driverCreate = service.save(driver);
        return ResponseEntity.status(HttpStatus.CREATED).body(driverCreate);
    }

}

