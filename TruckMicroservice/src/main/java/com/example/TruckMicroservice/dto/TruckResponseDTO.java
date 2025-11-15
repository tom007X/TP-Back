package com.example.TruckMicroservice.dto;

import com.example.TruckMicroservice.model.Truck;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TruckResponseDTO {

    private Long id;

    private String licensePlate;

    private float maxWeight;

    private float maxVolume;

    private float costPerKm;

    private float fuelConsuptiomPerKm;

    private boolean isAvailable;

    private DriverResponseDTO driver;

    public static TruckResponseDTO toDTO(Truck truck){
        DriverResponseDTO driverDTO = truck.getDriver() != null ? DriverResponseDTO.toDTO(truck.getDriver()) : null;

        return TruckResponseDTO.builder()
                         .id(truck.getId())
                         .licensePlate(truck.getLicensePlate())
                         .maxWeight(truck.getMaxWeight())
                         .maxVolume(truck.getMaxVolume())
                         .costPerKm(truck.getCostPerKm())
                         .fuelConsuptiomPerKm(truck.getFuelConsuptiomPerKm())
                         .isAvailable(truck.isAvailable())
                         .driver(driverDTO)
                         .build();

    }

}
