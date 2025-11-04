package com.example.DemoTruck.service;

import com.example.DemoTruck.exception.DriverNotFoundException;
import com.example.DemoTruck.model.Driver;
import com.example.DemoTruck.repository.DriverRepositoryImp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverService implements ServiceInterface<Driver,Long>{

    private final DriverRepositoryImp repositoryImp;


    @Override
    public Driver save(Driver entity) {
        return repositoryImp.save(entity);
    }

    @Override
    public Driver findById(Long id) {
        return repositoryImp.findById(id).orElseThrow(()-> new DriverNotFoundException(id));
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
