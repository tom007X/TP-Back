package com.example.ShippingMicroservice.dto.tariff;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TariffAvailabilityRequestDTO {

    @NotBlank
    private String subject;

    @NotBlank
    private String metric;

    @NotNull
    private Boolean available;
}