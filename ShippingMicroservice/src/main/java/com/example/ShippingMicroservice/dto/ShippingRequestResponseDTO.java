package com.example.ShippingMicroservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.ShippingMicroservice.model.ShippingRequestStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShippingRequestResponseDTO {
    private Long id;
    private LocalDateTime requestDatetime;
    private BigDecimal estimatedCost;
    private String estimatedTime;
    private BigDecimal finalCost;
    private String realTime;
    private ShippingRequestStatus status;
    private Long containerId;
    private Long clientId;
    private RouteDTO route;
}
