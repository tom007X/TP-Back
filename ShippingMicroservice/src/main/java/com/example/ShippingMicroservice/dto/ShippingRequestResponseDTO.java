package com.example.ShippingMicroservice.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import com.example.ShippingMicroservice.model.ShippingRequest;
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
    private String containerCode;
    private Long clientId;
    private RouteDTO route;

    public static ShippingRequestResponseDTO fromEntity(ShippingRequest request) {
        RouteDTO routeDTO = request.getRoute() != null ? RouteDTO.fromEntity(request.getRoute()) : null;

        return ShippingRequestResponseDTO.builder()
                .id(request.getId())
                .requestDatetime(request.getRequestDatetime())
                .estimatedCost(request.getEstimatedCost().setScale(2, RoundingMode.HALF_UP))
                .estimatedTime(request.getEstimatedTime())
                .finalCost(request.getFinalCost())
                .realTime(request.getRealTime())
                .status(request.getStatus())
                .containerCode(request.getContainer() != null ? request.getContainer().getCode() : null)
                .clientId(request.getClientId())
                .route(routeDTO)
                .build();
    }
}
