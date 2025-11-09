package com.example.ShippingMicroservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RouteDTO {
    private Long id;
    private Integer numDeposits;
    private Integer numSections;
    private Double totalDistance;
}
