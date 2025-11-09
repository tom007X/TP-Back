package com.example.TruckMicroservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.TruckMicroservice.model.Truck;
import com.example.TruckMicroservice.service.TruckService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/trucks")
@RequiredArgsConstructor
public class TruckController {
    private final TruckService serviceTruck;
        
    @GetMapping("/{id}")
    public ResponseEntity<Truck> getTruckById(@PathVariable Long id) {
        Truck truck = serviceTruck.findById(id);
        if(truck == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(truck);
    }

    @GetMapping
    public ResponseEntity<List<Truck>> getAllTruck() {
        List<Truck> trucks = serviceTruck.findAll();

        if(trucks.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(trucks);
    }

    @PostMapping
    public ResponseEntity<?> createTruck(@Valid @RequestBody Truck truck) {
        Truck truckCreate = serviceTruck.save(truck);
        return ResponseEntity.status(HttpStatus.CREATED).body(truckCreate);
    }

    @PatchMapping("/{id}/availability")
    public ResponseEntity<Truck> updateAvailibility(@PathVariable Long id,
                                                    @RequestParam boolean available){
        Truck truckUpdate = serviceTruck.updateAvailability(id,available);
        return ResponseEntity.ok(truckUpdate);

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTruck(@PathVariable Long id){
        serviceTruck.deleteTruckById(id);
        return ResponseEntity.noContent().build();
    }
}
