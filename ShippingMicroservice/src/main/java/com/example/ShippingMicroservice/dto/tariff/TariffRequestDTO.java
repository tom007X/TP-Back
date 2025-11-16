package com.example.ShippingMicroservice.dto.tariff;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class TariffRequestDTO {

    @NotBlank
    private String subject;

    @NotBlank
    private String metric;

    @NotNull
    @Positive
    private BigDecimal value;
}