package com.example.ShippingMicroservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ShippingMicroservice.model.ShippingRequest;

public interface ShippingRequestRepositoryImpl extends JpaRepository<ShippingRequest, Long>{

}
