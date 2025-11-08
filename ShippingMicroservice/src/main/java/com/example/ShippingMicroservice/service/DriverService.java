package com.example.ShippingMicroservice.service;

import com.example.ShippingMicroservice.exception.NotFoundException;
import com.example.ShippingMicroservice.model.Driver;
import com.example.ShippingMicroservice.repository.DriverRepositoryImp;
import com.example.ShippingMicroservice.service.interfaces.BasicService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverService implements BasicService<Driver,Long>{

    private final DriverRepositoryImp repositoryImp;


    @Override
    public Driver save(Driver entity) {
        return repositoryImp.save(entity);
    }

    @Override
    public Driver findById(Long id) {
        return repositoryImp.findById(id).orElseThrow(()-> new NotFoundException(Driver.class.getSimpleName(), id));
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
