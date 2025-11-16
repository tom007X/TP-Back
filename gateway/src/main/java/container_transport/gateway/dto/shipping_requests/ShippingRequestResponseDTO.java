package container_transport.gateway.dto.shipping_requests;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShippingRequestResponseDTO {
    private Long id;
    private LocalDateTime requestDatetime;
    private BigDecimal estimatedCost;
    private String estimatedTime;
    private BigDecimal finalCost;
    private String realTime;
    private String status;
    private String containerCode;
    private Long clientId;
    private RouteDTO route;
}
