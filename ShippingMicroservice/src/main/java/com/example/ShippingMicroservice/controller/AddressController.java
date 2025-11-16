package com.example.ShippingMicroservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.ShippingMicroservice.dto.address.AddressRequestDTO;
import com.example.ShippingMicroservice.dto.address.AddressResponseDTO;
import com.example.ShippingMicroservice.dto.address.CoordinateDTO;
import com.example.ShippingMicroservice.service.AddressService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService service;

    @GetMapping
    public ResponseEntity<List<AddressResponseDTO>> findAll() {
        List<AddressResponseDTO> addresses = service.findAll();
        if (addresses.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(addresses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressResponseDTO> findOne(@PathVariable Long id) {
        AddressResponseDTO address = service.findById(id);
        return ResponseEntity.ok(address);
    }

    @PostMapping
    public ResponseEntity<AddressResponseDTO> create(@Valid @RequestBody AddressRequestDTO dto) {
        AddressResponseDTO created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressResponseDTO> update(@PathVariable Long id, @Valid @RequestBody AddressRequestDTO dto) {
        AddressResponseDTO updated = service.update(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/coordinates")
    public ResponseEntity<List<CoordinateDTO>> allCoordinates() {
        List<CoordinateDTO> coords = service.findAllCoordinates();
        if (coords.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(coords);
    }

}
