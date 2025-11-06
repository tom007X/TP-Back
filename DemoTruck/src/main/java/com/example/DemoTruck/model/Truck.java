package com.example.DemoTruck.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Entity
@Table(name = "TRUCK")
public class Truck {

    //AGREGAR UN PATRON DE PATENTE.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "truck_id")
    private Long id;

    @Column(name = "license_plate", nullable = false, unique = true)
    @Pattern(regexp = "(^[A-Z]{3}\\d{3}$)|(^[A-Z]{2}\\d{3}[A-Z]{2}$)", flags = Pattern.Flag.CASE_INSENSITIVE,
        message = "Formato de patente inválido")
    @NotBlank
    private String licensePlate;

    @Column(name = "max_weight", nullable = false)
    @Positive
    private float maxWeight;

    @Column(name = "max_volume", nullable = false)
    @Positive
    private float maxVolume;

    @Column(name = "cost_per_km")
    @Positive
    private float costPerKm;

    @Column(name = "fuel_consuptiom_per_km")
    @Positive
    private float fuelConsuptiomPerKm;

    @Column(name = "is_available")
    private boolean isAvailable;

    @ManyToOne()
    @JoinColumn(name = "driver_id")
    private Driver driver;

}
