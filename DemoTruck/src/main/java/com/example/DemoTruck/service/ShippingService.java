package com.example.DemoTruck.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.DemoTruck.exception.NotFoundException;
import com.example.DemoTruck.model.ShippingRequest;
import com.example.DemoTruck.repository.ShippingRequestRepositoryImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShippingService implements ServiceInterface<ShippingRequest, Long>{
    
    private final ShippingRequestRepositoryImpl repository;
    
    @Override
    public ShippingRequest save(ShippingRequest entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public ShippingRequest findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(ShippingRequest.class.getSimpleName(), id));
    }

    @Override
    public List<ShippingRequest> findAll() {
        return repository.findAll();
    }

    @Override
    public void remove(ShippingRequest entity) {
        repository.delete(entity);
    }

}
