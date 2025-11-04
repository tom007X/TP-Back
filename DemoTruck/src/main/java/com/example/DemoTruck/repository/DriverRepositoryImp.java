package com.example.DemoTruck.repository;

import com.example.DemoTruck.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DriverRepositoryImp extends JpaRepository<Driver,Long> {
}
