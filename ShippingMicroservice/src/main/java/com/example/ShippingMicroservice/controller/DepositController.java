package com.example.ShippingMicroservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ShippingMicroservice.dto.DepositRequestDTO;
import com.example.ShippingMicroservice.dto.DepositResponseDTO;
import com.example.ShippingMicroservice.service.DepositService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/deposits")
@RequiredArgsConstructor
public class DepositController {
    private final DepositService depositService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<DepositResponseDTO> list = depositService.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        DepositResponseDTO found = depositService.findById(id);
        return ResponseEntity.ok(found);
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody DepositRequestDTO dto,
            HttpServletRequest request) {
        DepositResponseDTO created = depositService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
            @Valid @RequestBody DepositRequestDTO dto) {
        DepositResponseDTO updated = depositService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        depositService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
