package com.example.ShippingMicroservice.dto.shipping_request;

import com.example.ShippingMicroservice.model.Route;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RouteDTO {
    private Long id;
    private Integer numDeposits;
    private Integer numSections;
    private Double totalDistance;
    private SectionDTO[] sections;

    public static RouteDTO fromEntity(Route route) {
        SectionDTO[] sectionDTOs = route.getSections().stream()
                .map(SectionDTO::fromEntity)
                .toArray(SectionDTO[]::new);
        return RouteDTO.builder()
                .id(route.getId())
                .numDeposits(route.getNumDeposit())
                .numSections(route.getNumSections())
                .totalDistance(route.getTotalDistance())
                .sections(sectionDTOs)
                .build();
    }

}
