package com.example.ShippingMicroservice.dto;

import java.time.LocalDateTime;

import com.example.ShippingMicroservice.model.Section;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SectionDTO {
    private Long id;
    private LocalDateTime dateTimeStart;
    private LocalDateTime dateTimeEnd;
    private Double distance;
    private String assignatedTruck;
    private String start;
    private String end;
    private String status;
    private String type;


    public static SectionDTO fromEntity(Section s) {
        return SectionDTO.builder()
            .id(s.getId())
            .dateTimeStart(s.getDatetimeStart())
            .dateTimeEnd(s.getDatetimeEnd())
            .distance(s.getDistance())
            .assignatedTruck(s.getTruckId() != null ? s.getTruckId().toString() : null)
            .start(s.getStartLocation())
            .end(s.getEndLocation())
            .status(s.getStatus().getValue())
            .type(s.getType().getValue())
            .build();
    }
}
