package com.example.ShippingMicroservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ShippingMicroservice.model.SectionStatus;

public interface SectionStatusRepositoryImpl extends JpaRepository<SectionStatus, Long>{

}
