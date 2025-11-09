package com.example.TruckMicroservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.TruckMicroservice.model.Truck;

public interface TruckRepositoryImp extends JpaRepository<Truck, Long> {
    
}
