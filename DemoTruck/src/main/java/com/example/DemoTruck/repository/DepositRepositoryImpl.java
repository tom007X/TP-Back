package com.example.DemoTruck.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.DemoTruck.model.Deposit;

public interface DepositRepositoryImpl extends JpaRepository<Deposit, Long> {

}
