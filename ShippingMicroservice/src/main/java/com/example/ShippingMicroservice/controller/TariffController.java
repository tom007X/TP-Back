package com.example.ShippingMicroservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ShippingMicroservice.dto.tariff.TariffAvailabilityRequestDTO;
import com.example.ShippingMicroservice.dto.tariff.TariffDTO;
import com.example.ShippingMicroservice.dto.tariff.TariffRequestDTO;
import com.example.ShippingMicroservice.service.TariffService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/tariffs")
@RequiredArgsConstructor
public class TariffController {

    private final TariffService tariffService;

    // CREATE
    // POST /api/v1/tariffs
    @PostMapping
    public ResponseEntity<TariffDTO> create(@Valid @RequestBody TariffRequestDTO request) {
        TariffDTO created = tariffService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // GET ALL
    // GET /api/v1/tariffs
    @GetMapping
    public ResponseEntity<List<TariffDTO>> getAll() {
        return ResponseEntity.ok(tariffService.findAll());
    }

    // GET BY ID
    // GET /api/v1/tariffs/{id}
    @GetMapping("/{id}")
    public ResponseEntity<TariffDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(tariffService.findById(id));
    }

    // UPDATE VALUE by subject + metric
    // PUT /api/v1/tariffs/value
    @PutMapping("/value")
    public ResponseEntity<TariffDTO> updateValue(
            @Valid @RequestBody TariffRequestDTO request) {
        TariffDTO updated = tariffService.updateValueBySubjectAndMetric(request);
        return ResponseEntity.ok(updated);
    }

    // ENABLE/DISABLE by subject + metric
    // PATCH /api/v1/tariffs/availability
    @PatchMapping("/availability")
    public ResponseEntity<TariffDTO> updateAvailability(
            @Valid @RequestBody TariffAvailabilityRequestDTO request) {
        TariffDTO updated = tariffService.updateAvailability(request);
        return ResponseEntity.ok(updated);
    }

    // DELETE BY ID
    // DELETE /api/v1/tariffs/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tariffService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
