package com.example.DemoTruck.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.DemoTruck.model.ContainerStatus;

public interface ContainerStatusRepositoryImpl extends JpaRepository<ContainerStatus, Long> {

}
