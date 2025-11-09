package com.example.ShippingMicroservice.dto;

import java.math.BigDecimal;

import com.example.ShippingMicroservice.model.Deposit;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DepositResponseDTO {
    private Long id;
    private String name;
    private BigDecimal dailyStorageCost;
    private AddressResponseDTO address;

    static public DepositResponseDTO fromEntity(Deposit e) {
        return DepositResponseDTO.builder()
                .id(e.getId())
                .name(e.getName())
                .dailyStorageCost(e.getDailyStorageCost())
                .address(AddressResponseDTO.fromEntity(e.getAddress()))
                .build();
    }
}
