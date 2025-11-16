package com.example.ShippingMicroservice.dto.container;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ContainerRequestDTO {

    @NotBlank
    private String code;
    @Positive
    private Double weight;
    @Positive
    private Double volume;

}
