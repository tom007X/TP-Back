package com.example.ShippingMicroservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ShippingMicroservice.exception.NotFoundException;
import com.example.ShippingMicroservice.model.ShippingRequest;
import com.example.ShippingMicroservice.repository.ShippingRequestRepositoryImpl;

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
