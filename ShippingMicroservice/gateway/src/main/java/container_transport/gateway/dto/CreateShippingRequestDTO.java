package container_transport.gateway.dto;

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
    
    @NotNull(message = "Container ID is required")
    private Long containerId;
    
    @NotNull(message = "Client ID is required")
    private Long clientId;

}
