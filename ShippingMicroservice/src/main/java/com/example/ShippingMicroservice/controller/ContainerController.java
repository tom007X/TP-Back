package com.example.ShippingMicroservice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ShippingMicroservice.dto.container.ContainerDTO;
import com.example.ShippingMicroservice.model.ContainerStatus;
import com.example.ShippingMicroservice.service.ContainerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/containers")
@RequiredArgsConstructor
public class ContainerController {
    private final ContainerService containerService;

    @GetMapping
    public ResponseEntity<List<ContainerDTO>> getAllContainers() {
        List<ContainerDTO> containers = containerService.findAll();
        return ResponseEntity.ok(containers);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ContainerDTO>> getContainersByStatus(@PathVariable ContainerStatus status) {
        List<ContainerDTO> containers = containerService.findByStatus(status);
        return ResponseEntity.ok(containers);
    }
}
