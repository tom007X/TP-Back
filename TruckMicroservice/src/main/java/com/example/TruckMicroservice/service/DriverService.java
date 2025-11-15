package com.example.TruckMicroservice.service;

import java.util.List;
import java.util.stream.Collectors;

import com.example.TruckMicroservice.dto.DriverRequestDTO;
import com.example.TruckMicroservice.dto.DriverResponseDTO;

import org.springframework.stereotype.Service;

import com.example.TruckMicroservice.exception.NotFoundException;
import com.example.TruckMicroservice.model.Driver;
import com.example.TruckMicroservice.repository.DriverRepositoryImp;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DriverService{
    private final DriverRepositoryImp repositoryImp;

    @Transactional
    public DriverResponseDTO createDriverDTO(DriverRequestDTO driverRequestDTO) {

        Driver newDriver = Driver.builder()
                .name(driverRequestDTO.getName())
                .surname(driverRequestDTO.getSurname())
                .phone(driverRequestDTO.getPhone())
                .build();

        repositoryImp.save(newDriver);
        return DriverResponseDTO.toDTO(newDriver);
    }

    @Transactional(readOnly = true)
    public DriverResponseDTO findDriverDTOById(Long id) {
        Driver findDriver = repositoryImp.findById(id).orElseThrow(() -> new NotFoundException(Driver.class.getSimpleName(), id));
        return DriverResponseDTO.toDTO(findDriver);
    }

    @Transactional(readOnly = true)
    public Driver findDriverEntityById(Long id) {
        return repositoryImp.findById(id)
                .orElseThrow(() -> new NotFoundException(Driver.class.getSimpleName(), id));
    }

    @Transactional(readOnly = true)
    public List<DriverResponseDTO> findDriversDTOAll() {
        List<Driver> drivers = repositoryImp.findAll();
        return drivers.stream().map(DriverResponseDTO::toDTO).collect(Collectors.toList());
    }

    public boolean existDriverById(Long id){
        return repositoryImp.existsById(id);
    }

    @Transactional
    public void deleteDriverById(Long id) {
        Driver driver = repositoryImp.findById(id)
                .orElseThrow(() -> new NotFoundException(Driver.class.getSimpleName(), id));
        if (repositoryImp.hasTrucks(id)){
            throw new IllegalStateException("Cannot delete driver with assigned trucks");
        }
        repositoryImp.deleteById(id);
    }
}
