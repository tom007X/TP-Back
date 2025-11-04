package com.example.DemoTruck.controller;


import com.example.DemoTruck.model.Truck;
import com.example.DemoTruck.service.TruckService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trucks")
@RequiredArgsConstructor
public class TruckController {

    private final TruckService serviceTruck;

    @GetMapping("/{id}")
    public ResponseEntity<Truck> getTruckById(@PathVariable Long id){
        Truck truck = serviceTruck.findById(id);
        if (truck == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(truck);
    }

    @GetMapping
    public ResponseEntity<List<Truck>> getAllTruck(){
        List<Truck> trucks = serviceTruck.findAll();
        if (trucks.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(trucks);
    }

    @PostMapping
    public ResponseEntity<?> createTruck(@Valid @RequestBody Truck truck){
        Truck truckCreate = serviceTruck.save(truck);
        return ResponseEntity.status(HttpStatus.CREATED).body(truckCreate);
    }
}
