package com.example.TruckMicroservice.controller;

import java.util.List;

import com.example.TruckMicroservice.dto.TruckRequestDTO;
import com.example.TruckMicroservice.dto.TruckResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.TruckMicroservice.service.TruckService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/trucks")
@RequiredArgsConstructor
public class TruckController {
    private final TruckService serviceTruck;
        
    @GetMapping("/{id}")
    public ResponseEntity<TruckResponseDTO> getTruckById(@PathVariable Long id) {
        TruckResponseDTO truck = serviceTruck.findById(id);
        if(truck == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(truck);
    }

    @GetMapping
    public ResponseEntity<List<TruckResponseDTO>> getAllTruck() {
        List<TruckResponseDTO> trucks = serviceTruck.findAll();

        if(trucks.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(trucks);
    }

    @PostMapping
    public ResponseEntity<TruckResponseDTO> createTruck(@Valid @RequestBody TruckRequestDTO truck) {
        TruckResponseDTO truckCreate = serviceTruck.create(truck);
        return ResponseEntity.status(HttpStatus.CREATED).body(truckCreate);
    }

    @PatchMapping("/{id}/assign/{idDriver}")
    public ResponseEntity<TruckResponseDTO> assignDriver(@PathVariable("id") Long idTruck,
                                                         @PathVariable("idDriver") Long idDriver){
        TruckResponseDTO truckResponse = serviceTruck.assignDriver(idTruck, idDriver);
        return ResponseEntity.ok(truckResponse);
    }

    @PatchMapping("/{id}/availability")
    public ResponseEntity<TruckResponseDTO> updateAvailibility(@PathVariable Long id,
                                                    @RequestParam boolean available){
        TruckResponseDTO truckUpdate = serviceTruck.updateAvailability(id,available);
        return ResponseEntity.ok(truckUpdate);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTruck(@PathVariable Long id){
        serviceTruck.deleteTruckById(id);
        return ResponseEntity.noContent().build();
    }
}
