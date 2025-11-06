package com.example.ShippingMicroservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ShippingMicroservice.model.ShippingRequestStatus;

public interface ShippingRequestStatusRepositoryImpl extends JpaRepository<ShippingRequestStatus, Long>{

}
