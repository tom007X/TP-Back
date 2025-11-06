package com.example.ShippingMicroservice.repository;

import com.example.ShippingMicroservice.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DriverRepositoryImp extends JpaRepository<Driver,Long> {
}
