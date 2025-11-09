package com.example.TruckMicroservice.service;
import com.example.TruckMicroservice.exception.NotFoundException;
import com.example.TruckMicroservice.model.Truck;
import com.example.TruckMicroservice.repository.TruckRepositoryImp;
import com.example.TruckMicroservice.service.interfaces.BasicService;

import java.util.List;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TruckService implements BasicService<Truck, Long>{
    
    private final TruckRepositoryImp repositoryImp;

    @Override
    public Truck save(Truck entity) {
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
