package com.example.DemoTruck.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.DemoTruck.model.Tariff;

public interface TariffRepositoryImpl extends JpaRepository<Tariff, Long>{

}
