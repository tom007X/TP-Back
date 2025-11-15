package com.example.TruckMicroservice.dto;

import com.example.TruckMicroservice.model.Driver;
import com.example.TruckMicroservice.model.Truck;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TruckRequestDTO {

    @Pattern(regexp = "(^[A-Z]{3}\\d{3}$)|(^[A-Z]{2}\\d{3}[A-Z]{2}$)", flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Formato de patente inválido")
    @NotBlank
    private String licensePlate;

    @Positive(message = "Maximum weight is positive")
    private float maxWeight;

    @Positive(message = "Maximum volume is positive")
    private float maxVolume;

    @Positive(message = "The cost per km is positive")
    private float costPerKm;

    @Positive(message = "The fuel consuptiom per km is positive")
    private float fuelConsuptiomPerKm;

    private boolean isAvailable;

    private Long driverId;

    private DriverRequestDTO driver;

    @AssertTrue(message = "Provide either driverId or driver, not both.")
    public boolean isDriverReferenceExclusive() {
        return driverId == null || driver == null;
    }

    public Truck toEntity() {
        Truck truck = new Truck();
        truck.setLicensePlate(this.licensePlate);
        truck.setMaxWeight(this.maxWeight);
        truck.setMaxVolume(this.maxVolume);
        truck.setCostPerKm(this.costPerKm);
        truck.setFuelConsuptiomPerKm(this.fuelConsuptiomPerKm);
        truck.setAvailable(this.isAvailable);

        // 👇 Asociamos solo el id del Driver si está presente
         if (this.driverId != null) {
            Driver d = new Driver();
            d.setId(this.driverId);
            truck.setDriver(d);
        } else if (this.driver != null) {
            truck.setDriver(this.driver.toEntity());
        }

        return truck;
    }


}
