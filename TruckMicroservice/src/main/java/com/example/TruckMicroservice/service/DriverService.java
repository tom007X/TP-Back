package com.example.TruckMicroservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.TruckMicroservice.exception.NotFoundException;
import com.example.TruckMicroservice.model.Driver;
import com.example.TruckMicroservice.repository.DriverRepositoryImp;
import com.example.TruckMicroservice.service.interfaces.BasicService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DriverService implements BasicService<Driver, Long> {
    private final DriverRepositoryImp repositoryImp;

    @Override
    public Driver save(Driver entity) {
        return repositoryImp.save(entity);
    }

    @Override
    public Driver findById(Long id) {
        return repositoryImp.findById(id).orElseThrow(() -> new NotFoundException(Driver.class.getSimpleName(), id));
    }

    @Override
    public List<Driver> findAll() {
        return repositoryImp.findAll();
    }

    @Override
    public void remove(Driver entity) {
        repositoryImp.delete(entity);
    }
}
