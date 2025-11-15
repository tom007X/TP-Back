package com.example.TruckMicroservice.controller;

import java.util.List;

import com.example.TruckMicroservice.dto.DriverRequestDTO;
import com.example.TruckMicroservice.dto.DriverResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.TruckMicroservice.service.DriverService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService serviceDriver;

    @GetMapping("/{id}")
    public ResponseEntity<DriverResponseDTO> getDriverById(@PathVariable Long id) {
        DriverResponseDTO driverResponse = serviceDriver.findDriverDTOById(id);

        if(driverResponse == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(driverResponse);
    }

    @GetMapping
    public ResponseEntity<List<DriverResponseDTO>> getAllDriver() {
        List<DriverResponseDTO> driversReponse = serviceDriver.findDriversDTOAll();
        if(driversReponse.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(driversReponse);
    }

    @PostMapping
    public ResponseEntity<DriverResponseDTO> createDriver(@Valid @RequestBody DriverRequestDTO driverDTO) {
        DriverResponseDTO driverResponse = serviceDriver.create(driverDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(driverResponse);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDriver(@PathVariable Long id){
        serviceDriver.deleteDriverById(id);
        return ResponseEntity.noContent().build();
    }
}
