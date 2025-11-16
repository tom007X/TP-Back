package com.example.ShippingMicroservice.dto.deposit;

import java.math.BigDecimal;

import com.example.ShippingMicroservice.dto.address.AddressRequestDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class DepositRequestDTO {
    @NotBlank
    private String name;

    private Long addressId;

    @Valid
    private AddressRequestDTO address;
    
    @NotNull
    @Positive
    private BigDecimal dailyStorageCost;

    @AssertTrue(message = "Provide either addressId or address data, but not both.")
    public boolean isValidAddressSpecification() {
        boolean hasId = addressId != null;
        boolean hasObj = address != null;
        return hasId ^ hasObj;
    }
}
