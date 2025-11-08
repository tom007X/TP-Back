package com.example.ShippingMicroservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DepositRequestDTO {
    @NotBlank
    private String name;

    private Long addressId;

    @Valid
    private AddressRequestDTO address;

    @AssertTrue(message = "Provide either addressId or address data, but not both.")
    public boolean isValidAddressSpecification() {
        boolean hasId = addressId != null;
        boolean hasObj = address != null;
        return hasId ^ hasObj;
    }
}
