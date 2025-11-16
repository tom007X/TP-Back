package container_transport.gateway.dto.shipping_requests;

import lombok.Data;

@Data
public class AsignTruckDTO {
    private Long truckId;
    private String truckPlate;
    private double truckCostPerKm;
}