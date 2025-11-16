package com.example.ShippingMicroservice.dto.shipping_request;

import com.example.ShippingMicroservice.dto.address.AddressRequestDTO;
import com.example.ShippingMicroservice.dto.container.ContainerRequestDTO;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateShippingRequestDTO {
    @NotNull(message = "Start address is required")
    private AddressRequestDTO startAddress;
    
    @NotNull(message = "End address is required")
    private AddressRequestDTO endAddress;
    
    @NotNull(message = "Client ID is required")
    private Long clientId;
    
    private Long containerId;

    private ContainerRequestDTO container;


    @AssertTrue(message = "Provide either containerId or container, not both.")
    public boolean isContainerReferenceExclusive() {
        return containerId  == null || container == null;
    }

}
