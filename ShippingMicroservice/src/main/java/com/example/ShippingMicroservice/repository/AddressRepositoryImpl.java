package com.example.ShippingMicroservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.ShippingMicroservice.dto.CoordinateDTO;
import com.example.ShippingMicroservice.model.Address;

public interface AddressRepositoryImpl extends JpaRepository<Address, Long> {

    @Query("""
            select new com.example.ShippingMicroservice.dto.CoordinateDTO(a.latitude, a.longitude)
            from Address a
            where a.latitude is not null and a.longitude is not null
            """)
    List<CoordinateDTO> findAllCoordinates();

}
