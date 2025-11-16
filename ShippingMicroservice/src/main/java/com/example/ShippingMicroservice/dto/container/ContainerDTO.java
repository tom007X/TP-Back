package com.example.ShippingMicroservice.dto.container;

import com.example.ShippingMicroservice.model.Container;

import lombok.Data;

@Data
public class ContainerDTO {
    private Double weight;
    private Double volume;
    private String code;
    private String status;

    public static ContainerDTO fromEntity(Container container) {
        ContainerDTO dto = new ContainerDTO();
        dto.setCode(container.getCode());
        dto.setWeight(container.getWeight().doubleValue());
        dto.setVolume(container.getVolume().doubleValue());
        dto.setStatus(container.getStatus().getValue());
        return dto;
    }
}
