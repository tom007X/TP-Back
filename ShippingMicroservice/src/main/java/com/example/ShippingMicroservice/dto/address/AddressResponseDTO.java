package com.example.ShippingMicroservice.dto.address;

import com.example.ShippingMicroservice.model.Address;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressResponseDTO {
    private Long id;
    private String city;
    private String postalCode;
    private String street;
    private String number;
    private Double latitude;
    private Double longitude;

    public static AddressResponseDTO fromEntity(Address e) {
        return AddressResponseDTO.builder()
                .id(e.getId())
                .city(e.getCity())
                .postalCode(e.getPostalCode())
                .street(e.getStreet())
                .number(e.getNumber())
                .latitude(e.getLatitude())
                .longitude(e.getLongitude())
                .build();
    }

    public static Address toEntity(AddressResponseDTO dto) {
        return Address.builder()
                .id(dto.getId())
                .city(dto.getCity())
                .postalCode(dto.getPostalCode())
                .street(dto.getStreet())
                .number(dto.getNumber())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .build();
    }
}
