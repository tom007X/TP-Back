package com.example.TruckMicroservice.service;
import com.example.TruckMicroservice.dto.TruckRequestDTO;
import com.example.TruckMicroservice.dto.TruckResponseDTO;
import com.example.TruckMicroservice.exception.NotFoundException;
import com.example.TruckMicroservice.exception.TruckDuplicateLicensePlate;
import com.example.TruckMicroservice.model.Driver;
import com.example.TruckMicroservice.model.Truck;
import com.example.TruckMicroservice.repository.TruckRepositoryImp;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TruckService {
    
    private final TruckRepositoryImp repositoryImp;
    private final DriverService driverService;

    @Transactional
    public TruckResponseDTO create(TruckRequestDTO truckRequestDTO) {

        boolean exists = repositoryImp.existsByLicensePlate(truckRequestDTO.getLicensePlate());
        if (exists)
            throw new TruckDuplicateLicensePlate(truckRequestDTO.getLicensePlate());

        Truck newTruck = truckRequestDTO.toEntity();

        if(newTruck.getDriver() != null ) {
            if(newTruck.getDriver().getId() != null) {
                if (driverService.existDriverById(newTruck.getDriver().getId())){
                    Driver driver = driverService.findDriverEntityById(newTruck.getDriver().getId());
                    newTruck.setDriver(driver);
                }else {
                    newTruck.setDriver(null);
                }
            }
        }

        repositoryImp.save(newTruck);
        return TruckResponseDTO.toDTO(newTruck);
    }

    @Transactional(readOnly = true)
    public TruckResponseDTO findById(Long id) {
        Truck truck = repositoryImp.findById(id).orElseThrow(() -> new NotFoundException(Truck.class.getSimpleName(), id));
        return TruckResponseDTO.toDTO(truck);
    }

    @Transactional(readOnly = true)
    public List<TruckResponseDTO> findAll() {
        return repositoryImp.findAll().stream().map(TruckResponseDTO::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public void deleteTruckById(Long id) {
        if (!repositoryImp.existsById(id)) {
            throw new EntityNotFoundException("Truck not found with id " + id);
        }
        repositoryImp.deleteById(id);
    }

    @Transactional
    public TruckResponseDTO updateAvailability(Long id, boolean available) {
        Truck truck = repositoryImp.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Truck not found with id " + id));

        truck.setAvailable(available);
        repositoryImp.save(truck);
        return TruckResponseDTO.toDTO(truck);
    }

    @Transactional
    public TruckResponseDTO assignDriver(Long idTruck, Long idDriver){
        Truck truck = repositoryImp.findById(idTruck)
                .orElseThrow(() -> new EntityNotFoundException("Truck not found with id " + idTruck));
        if (!driverService.existDriverById(idDriver)){
            throw new EntityNotFoundException("Driver not found with id " + idDriver);
        }
        Driver driver = driverService.findDriverEntityById(idDriver);

        truck.setDriver(driver);

        return TruckResponseDTO.toDTO(truck);
    }


}
