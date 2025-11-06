package com.example.DemoTruck.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.DemoTruck.model.Address;

public interface AddressRepositoryImpl extends JpaRepository<Address, Long>{

}
