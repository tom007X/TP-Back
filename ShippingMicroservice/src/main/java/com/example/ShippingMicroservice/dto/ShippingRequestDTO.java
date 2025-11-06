package com.example.ShippingMicroservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShippingRequestDTO {
    private Long clientId;
    private ContainerDTO container;
    private CoordinateDTO origin;
    private CoordinateDTO destination;

}
