package com.example.DemoTruck.repository;

import com.example.DemoTruck.model.Truck;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TruckRepositoryImp extends JpaRepository<Truck,Long> {

    Truck findByLicensePlate(String licensePlate);
    boolean existsByLicensePlate(String licensePlate);
}
