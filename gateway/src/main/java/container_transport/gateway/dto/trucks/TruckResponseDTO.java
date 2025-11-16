package container_transport.gateway.dto.trucks;

import lombok.Data;

@Data
public class TruckResponseDTO {
    private Long id;

    private String licensePlate;

    private float maxWeight;

    private float maxVolume;

    private float costPerKm;

    private float fuelConsuptiomPerKm;

    private boolean isAvailable;

    private DriverResponseDTO driver;
}
