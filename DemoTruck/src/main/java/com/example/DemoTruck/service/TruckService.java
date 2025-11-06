package com.example.DemoTruck.service;

import com.example.DemoTruck.exception.TruckDuplicateLicensePlate;
import com.example.DemoTruck.exception.NotFoundException;
import com.example.DemoTruck.model.Driver;
import com.example.DemoTruck.model.Truck;
import com.example.DemoTruck.repository.TruckRepositoryImp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TruckService implements ServiceInterface<Truck,Long>{

    private final TruckRepositoryImp repositoryImp;
    private final DriverService driverService;

    @Override
    public Truck save(Truck entity) {
        entity.setLicensePlate(entity.getLicensePlate().toUpperCase().trim());
        boolean exists = repositoryImp.existsByLicensePlate(entity.getLicensePlate());
        if (exists)
            throw new TruckDuplicateLicensePlate(entity.getLicensePlate());
        
        if(entity.getDriver() != null ) {
            if(entity.getDriver().getId() != null) {
                Driver driver = driverService.findById(entity.getDriver().getId());
                entity.setDriver(driver);
            } else {
                Driver newDriver = driverService.save(entity.getDriver());
                entity.setDriver(newDriver);
            }
        }
        return repositoryImp.save(entity);
    }

    @Override
    public Truck findById(Long id) {
        return repositoryImp.findById(id).orElseThrow(() -> new NotFoundException(Truck.class.getSimpleName(), id));
    }

    @Override
    public List<Truck> findAll() {
        return repositoryImp.findAll();
    }

    @Override
    public void remove(Truck entity) {
        repositoryImp.delete(entity);
    }
}
