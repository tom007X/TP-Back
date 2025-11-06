package com.example.DemoTruck.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.DemoTruck.model.Route;

public interface RouteRepositoryImpl extends JpaRepository<Route, Long>{

}
