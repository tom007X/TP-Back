package com.example.ShippingMicroservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ShippingMicroservice.model.Deposit;

public interface DepositRepositoryImpl extends JpaRepository<Deposit, Long> {
        boolean existsByNameIgnoreCase(String name);

        @Query("SELECT d FROM Deposit d WHERE " +
                        "6371 * acos(cos(radians(:latitude)) * cos(radians(d.address.latitude)) * " +
                        "cos(radians(d.address.longitude) - radians(:longitude)) + " +
                        "sin(radians(:latitude)) * sin(radians(d.address.latitude))) <= :radius")
        List<Deposit> findDepositsWithinRadius(
                        @Param("latitude") Double latitude,
                        @Param("longitude") Double longitude,
                        @Param("radius") Double radius);
}
