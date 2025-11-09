package com.example.TruckMicroservice.service;
import com.example.TruckMicroservice.exception.NotFoundException;
import com.example.TruckMicroservice.model.Truck;
import com.example.TruckMicroservice.repository.TruckRepositoryImp;
import com.example.TruckMicroservice.service.interfaces.BasicService;

import java.util.List;

import jakarta.persistence.EntityNotFoundException;
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

    public void deleteTruckById(Long id) {
        if (!repositoryImp.existsById(id)) {
            throw new EntityNotFoundException("Truck not found with id " + id);
        }
        repositoryImp.deleteById(id);
    }

    public Truck updateAvailability(Long id, boolean available) {
        Truck truck = repositoryImp.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Truck not found with id " + id));

        truck.setAvailable(available);
        return repositoryImp.save(truck);
    }


}
