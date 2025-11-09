package com.example.ShippingMicroservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ShippingMicroservice.exception.NotFoundException;
import com.example.ShippingMicroservice.model.ShippingRequest;
import com.example.ShippingMicroservice.repository.ShippingRequestRepository;
import com.example.ShippingMicroservice.service.interfaces.BasicService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShippingService implements BasicService<ShippingRequest, Long>{
    
    private final ShippingRequestRepository repository;
    
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
