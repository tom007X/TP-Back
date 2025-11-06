package com.example.DemoTruck.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.DemoTruck.model.Container;

public interface ContainerRepositoryImpl extends JpaRepository<Container, Long>{

}
