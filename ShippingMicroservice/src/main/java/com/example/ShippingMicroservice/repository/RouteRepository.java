package com.example.ShippingMicroservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ShippingMicroservice.model.Route;

public interface RouteRepository extends JpaRepository<Route, Long>{

}
