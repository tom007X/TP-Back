package com.example.ShippingMicroservice.dto.address;

import com.example.ShippingMicroservice.model.Address;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AddressRequestDTO {

    @NotEmpty
    private String city;
    @NotEmpty
    private String postalCode;

    @NotEmpty
    private String street;
    private String number;

    @Min(-90)
    @Max(90)
    private Double latitude;
    @Min(-180)
    @Max(180)
    private Double longitude;

    public static Address toEntity(AddressRequestDTO dto) {
        return Address.builder()
                .city(dto.getCity())
                .postalCode(dto.getPostalCode())
                .street(dto.getStreet())
                .number(dto.getNumber())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .build();
    }

}
