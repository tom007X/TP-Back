package com.example.ShippingMicroservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ShippingMicroservice.model.Address;

public interface AddressRepositoryImpl extends JpaRepository<Address, Long>{

}
