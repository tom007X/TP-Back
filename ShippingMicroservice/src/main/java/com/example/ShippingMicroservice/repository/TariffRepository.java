package com.example.ShippingMicroservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ShippingMicroservice.model.Tariff;

public interface TariffRepository extends JpaRepository<Tariff, Long>{

    Optional<Tariff> findBySubjectAndMetric(String subject, String metric);

    Optional<Tariff> findBySubjectAndMetricAndAvailableIsTrue(String subject, String metric);

}
