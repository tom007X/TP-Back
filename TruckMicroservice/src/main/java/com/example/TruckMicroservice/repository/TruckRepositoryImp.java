package com.example.TruckMicroservice.repository;

import com.example.TruckMicroservice.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.TruckMicroservice.model.Truck;

public interface TruckRepositoryImp extends JpaRepository<Truck, Long> {
    Truck findByLicensePlate(String licensePlate);
    boolean existsByLicensePlate(String licensePlate);

}
