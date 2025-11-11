package com.example.TruckMicroservice.repository;

import com.example.TruckMicroservice.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DriverRepositoryImp extends JpaRepository<Driver, Long>{
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Truck t WHERE t.driver.id = :driverId")
    boolean hasTrucks(@Param("driverId") Long driverId);

}
