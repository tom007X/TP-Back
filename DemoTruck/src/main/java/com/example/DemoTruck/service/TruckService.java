package com.example.DemoTruck.service;

import com.example.DemoTruck.exception.TruckDuplicateLicensePlate;
import com.example.DemoTruck.exception.TruckNotFoundException;
import com.example.DemoTruck.model.Truck;
import com.example.DemoTruck.repository.TruckRepositoryImp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TruckService implements ServiceInterface<Truck,Long>{

    private final TruckRepositoryImp repositoryImp;

    @Override
    public Truck save(Truck entity) {
        boolean existe = repositoryImp.existsByLicensePlate(entity.getLicensePlate());
        if (!existe) {
            return repositoryImp.save(entity);
        }else {
          throw new TruckDuplicateLicensePlate(entity.getLicensePlate());
        }
    }

    @Override
    public Truck findById(Long id) {
        return repositoryImp.findById(id).orElseThrow(() -> new TruckNotFoundException(id));
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
