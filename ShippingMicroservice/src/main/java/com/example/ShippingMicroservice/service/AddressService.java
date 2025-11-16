package com.example.ShippingMicroservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ShippingMicroservice.dto.address.AddressRequestDTO;
import com.example.ShippingMicroservice.dto.address.AddressResponseDTO;
import com.example.ShippingMicroservice.dto.address.CoordinateDTO;
import com.example.ShippingMicroservice.exception.NotFoundException;
import com.example.ShippingMicroservice.model.Address;
import com.example.ShippingMicroservice.repository.AddressRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository repository;

    public AddressResponseDTO create(AddressRequestDTO dto) {
        Address address = AddressRequestDTO.toEntity(dto);
        Address returned = repository.save(address);
        return AddressResponseDTO.fromEntity(returned);
    }

    public AddressResponseDTO update(Long id, AddressRequestDTO dto) {
        Address current = findEntityById(id);
        if (dto.getCity() != null) current.setCity(dto.getCity());
        if (dto.getPostalCode() != null) current.setPostalCode(dto.getPostalCode());
        if (dto.getStreet() != null) current.setStreet(dto.getStreet());
        if (dto.getNumber() != null) current.setNumber(dto.getNumber());
        if (dto.getLatitude() != null) current.setLatitude(dto.getLatitude());
        if (dto.getLongitude() != null) current.setLongitude(dto.getLongitude());
        Address returned = repository.save(current);
        return AddressResponseDTO.fromEntity(returned);
    }

    public AddressResponseDTO findById(Long id) {
        Address returned = findEntityById(id);
        return AddressResponseDTO.fromEntity(returned);
    }

    public List<AddressResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(AddressResponseDTO::fromEntity)
                .toList();
    }

    public void deleteById(Long id) {
        Address returned = findEntityById(id);
        repository.delete(returned);
    }

    public List<CoordinateDTO> findAllCoordinates() {
        return repository.findAllCoordinates();
    }

    public Address findEntityById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(Address.class.getSimpleName(), id));
    }

}
