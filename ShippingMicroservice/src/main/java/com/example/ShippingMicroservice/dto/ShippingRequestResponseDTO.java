package com.example.ShippingMicroservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.ShippingMicroservice.model.Route;
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

    public static ShippingRequestResponseDTO fromEntity(ShippingRequest request, Route route) {

        ShippingRequestResponseDTO res = ShippingRequestResponseDTO.fromEntity(request);
        RouteDTO routeDTO = route != null ? RouteDTO.fromEntity(route) : null;
        res.route = routeDTO;

        return res;
    }

    public static ShippingRequestResponseDTO fromEntity(ShippingRequest request) {
        return ShippingRequestResponseDTO.builder()
                .id(request.getId())
                .requestDatetime(request.getRequestDatetime())
                .estimatedCost(request.getEstimatedCost())
                .estimatedTime(request.getEstimatedTime())
                .finalCost(request.getFinalCost())
                .realTime(request.getRealTime())
                .status(request.getStatus())
                .containerCode(request.getContainer() != null ? request.getContainer().getCode() : null)
                .clientId(request.getClientId())
                .build();
    }
}
