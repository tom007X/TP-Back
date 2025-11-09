package com.example.TruckMicroservice.repository;

import com.example.TruckMicroservice.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepositoryImp extends JpaRepository<Driver, Long>{
    
}
