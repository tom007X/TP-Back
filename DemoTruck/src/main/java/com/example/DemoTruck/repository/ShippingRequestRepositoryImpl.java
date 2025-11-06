package com.example.DemoTruck.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.DemoTruck.model.ShippingRequest;

public interface ShippingRequestRepositoryImpl extends JpaRepository<ShippingRequest, Long>{

}
