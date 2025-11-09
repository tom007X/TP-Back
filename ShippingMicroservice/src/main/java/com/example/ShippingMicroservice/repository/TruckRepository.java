package com.example.ShippingMicroservice.repository;

import com.example.ShippingMicroservice.model.Truck;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TruckRepository extends JpaRepository<Truck,Long> {

    Truck findByLicensePlate(String licensePlate);
    boolean existsByLicensePlate(String licensePlate);
}
