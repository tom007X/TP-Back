package com.example.ShippingMicroservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ShippingMicroservice.model.Tariff;

public interface TariffRepository extends JpaRepository<Tariff, Long>{

}
