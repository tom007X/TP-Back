package com.example.ShippingMicroservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ShippingMicroservice.model.Container;

public interface ContainerRepositoryImpl extends JpaRepository<Container, Long> {
    boolean existsByCodeIgnoreCase(String code);


}
