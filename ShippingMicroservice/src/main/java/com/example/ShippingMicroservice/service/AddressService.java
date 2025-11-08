package com.example.ShippingMicroservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ShippingMicroservice.dto.AddressRequestDTO;
import com.example.ShippingMicroservice.dto.CoordinateDTO;
import com.example.ShippingMicroservice.exception.NotFoundException;
import com.example.ShippingMicroservice.model.Address;
import com.example.ShippingMicroservice.repository.AddressRepositoryImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepositoryImpl repository;

    public Address create(AddressRequestDTO dto) {
        Address address = Address.builder()
                .city(dto.getCity())
                .postalCode(dto.getPostalCode())
                .street(dto.getStreet())
                .number(dto.getNumber())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .build();
        return repository.save(address);
    }

    public Address findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(Address.class.getSimpleName(), id));
    }

    public List<Address> findAll() {
        return repository.findAll();
    }

    public void deleteById(Long id) {
        Address existing = findById(id);
        repository.delete(existing);
    }

    public List<CoordinateDTO> findAllCoordinates() {
        return repository.findAllCoordinates();
    }


}
