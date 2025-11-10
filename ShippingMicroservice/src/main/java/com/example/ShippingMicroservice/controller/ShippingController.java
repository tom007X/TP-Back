package com.example.ShippingMicroservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ShippingMicroservice.dto.AsignTruckDTO;
import com.example.ShippingMicroservice.dto.CreateShippingRequestDTO;
import com.example.ShippingMicroservice.dto.ShippingRequestResponseDTO;
import com.example.ShippingMicroservice.service.ShippingRequestService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/shipping-requests")
@RequiredArgsConstructor
public class ShippingController {

    private final ShippingRequestService shippingRequestService;

    @PostMapping
    public ResponseEntity<ShippingRequestResponseDTO> createShippingRequest(
            @Valid @RequestBody CreateShippingRequestDTO dto) {
        ShippingRequestResponseDTO response = shippingRequestService.createShippingRequest(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ShippingRequestResponseDTO> getShippingRequest(
            @PathVariable Long id,
            @RequestParam Long clientId) {

        ShippingRequestResponseDTO response = shippingRequestService.getShippingRequest(id, clientId);
        return ResponseEntity.ok(response);

    }

    @GetMapping
    public ResponseEntity<List<ShippingRequestResponseDTO>> getShippingRequestsByClient(
            @RequestParam Long clientId) {

        List<ShippingRequestResponseDTO> responses = shippingRequestService.getShippingRequestsByClient(clientId);
        return ResponseEntity.ok(responses);

    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<ShippingRequestResponseDTO> cancelShippingRequest(
            @PathVariable Long id,
            @RequestParam Long clientId) {

        ShippingRequestResponseDTO response = shippingRequestService.cancelShippingRequest(id, clientId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{requestId}/sections/{sectionId}/asign-truck")
    public ResponseEntity<?> asignTruckToSection(@PathVariable Long requestId, @PathVariable Long sectionId,
            @Valid @RequestBody AsignTruckDTO dto) {
        ShippingRequestResponseDTO response = shippingRequestService.asignTruckToSection(requestId, sectionId, dto);
        return ResponseEntity.ok().body(response);
    }
}
