package com.example.ShippingMicroservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ShippingMicroservice.model.ShippingRequest;

public interface ShippingRequestRepository extends JpaRepository<ShippingRequest, Long> {

    List<ShippingRequest> findByClientIdOrderByRequestDatetimeDesc(Long clientId);

    List<ShippingRequest> findByClientIdAndStatusNotOrderByRequestDatetimeDesc(
            Long clientId,
            int status);

    boolean existsByIdAndClientId(Long id, Long clientId);

}
