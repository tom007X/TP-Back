package com.example.ShippingMicroservice.dto.tariff;

import java.math.BigDecimal;

import com.example.ShippingMicroservice.model.Tariff;

import lombok.Data;

@Data
public class TariffDTO {

    private Long id;
    private String subject;
    private String metric;
    private BigDecimal value;
    private Boolean available;

    public static TariffDTO fromEntity(Tariff tariff) {
        TariffDTO dto = new TariffDTO();
        dto.setId(tariff.getId());
        dto.setSubject(tariff.getSubject());
        dto.setMetric(tariff.getMetric());
        dto.setValue(tariff.getValue());
        dto.setAvailable(tariff.getAvailable());
        return dto;
    }
}