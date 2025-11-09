package com.example.ShippingMicroservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ShippingMicroservice.model.ContainerStatus;

public interface ContainerStatusRepository extends JpaRepository<ContainerStatus, Long> {

}
