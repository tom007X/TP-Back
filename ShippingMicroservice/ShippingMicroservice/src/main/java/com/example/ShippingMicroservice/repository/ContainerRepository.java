package com.example.ShippingMicroservice.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ShippingMicroservice.model.Container;
import com.example.ShippingMicroservice.model.ContainerStatus;

public interface ContainerRepository extends JpaRepository<Container, Long> {
    boolean existsByCodeIgnoreCase(String code);

    Optional<Container> findByCode(String code);

    List<Container> findByStatus(ContainerStatus status);


}
