package com.example.ShippingMicroservice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ShippingMicroservice.dto.ShippingRequestDTO;
import com.example.ShippingMicroservice.model.ShippingRequest;
import com.example.ShippingMicroservice.service.ShippingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/shippings")
@RequiredArgsConstructor
public class ShippingController {
    private final ShippingService shippingService;

    @GetMapping("/{id}")
    public ResponseEntity<ShippingRequest> getRequestById(@PathVariable Long id){
        ShippingRequest request = shippingService.findById(id);
        if (request == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(request);
    }

    @GetMapping()
    public ResponseEntity<List<ShippingRequest>> getAllRequests(){
        List<ShippingRequest> request = shippingService.findAll();
        if (request.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(request);
    }

    @PostMapping()
    public ResponseEntity<?> createShipingRequest(@RequestBody ShippingRequestDTO request) {
        // TODO: Crear shipping Request
        return ResponseEntity.created(null).build();
    }

}
