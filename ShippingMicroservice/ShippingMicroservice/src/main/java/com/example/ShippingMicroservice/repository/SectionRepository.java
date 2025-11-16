package com.example.ShippingMicroservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ShippingMicroservice.model.Section;

public interface SectionRepository extends JpaRepository<Section, Long>{

}
