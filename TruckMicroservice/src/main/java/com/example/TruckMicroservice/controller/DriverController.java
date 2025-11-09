package com.example.TruckMicroservice.controller;

import java.util.List;

import com.example.TruckMicroservice.model.Truck;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.TruckMicroservice.model.Driver;
import com.example.TruckMicroservice.service.DriverService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService serviceDriver;

    @GetMapping("/{id}")
    public ResponseEntity<Driver> getDriverById(@PathVariable Long id) {
        Driver driver = serviceDriver.findById(id);

        if(driver == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(driver);
    }

    @GetMapping
    public ResponseEntity<List<Driver>> getAllDriver() {
        List<Driver> drivers = serviceDriver.findAll();
        if(drivers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(drivers);
    }

    @PostMapping
    public ResponseEntity<?> createDriver(@Valid @RequestBody Driver driver) {
        Driver createDriver = serviceDriver.save(driver);
        return ResponseEntity.status(HttpStatus.CREATED).body(createDriver);
    }

}
