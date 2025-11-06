package com.example.DemoTruck.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.DemoTruck.model.ShippingRequestStatus;

public interface ShippingRequestStatusRepositoryImpl extends JpaRepository<ShippingRequestStatus, Long>{

}
