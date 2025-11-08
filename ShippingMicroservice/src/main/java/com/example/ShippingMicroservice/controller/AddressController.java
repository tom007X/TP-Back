package com.example.ShippingMicroservice.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.ShippingMicroservice.dto.AddressRequestDTO;
import com.example.ShippingMicroservice.dto.CoordinateDTO;
import com.example.ShippingMicroservice.model.Address;
import com.example.ShippingMicroservice.service.AddressService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService service;

    @PostMapping
    public ResponseEntity<Address> create(@Valid @RequestBody AddressRequestDTO dto) {
        Address created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Address> findOne(@PathVariable Long id) {
        Address address = service.findById(id);
        return ResponseEntity.ok(address);
    }

    @GetMapping
    public ResponseEntity<List<Address>> findAll() {
        List<Address> addresses = service.findAll();
        if (addresses.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(addresses);
    }

    @GetMapping("/coordinates")
    public ResponseEntity<List<CoordinateDTO>> allCoordinates() {
        List<CoordinateDTO> coords = service.findAllCoordinates();
        if (coords.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(coords);
    }

}
