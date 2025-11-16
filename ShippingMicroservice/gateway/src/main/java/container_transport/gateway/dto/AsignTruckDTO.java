package container_transport.gateway.dto;

import lombok.Data;

@Data
public class AsignTruckDTO {
    private Long truckId;
    private String truckPlate;
    private double truckCostPerKm;
}