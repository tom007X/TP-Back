package com.example.ShippingMicroservice.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ShippingMicroservice.dto.ContainerRequestDTO;
import com.example.ShippingMicroservice.exception.NotFoundException;
import com.example.ShippingMicroservice.model.Container;
import com.example.ShippingMicroservice.model.Deposit;
import com.example.ShippingMicroservice.repository.ContainerRepositoryImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContainerService {
    private final ContainerRepositoryImpl containerRepository;

    @Transactional
    public Container create(ContainerRequestDTO dto) {

        Container container = Container.builder()
                .code(dto.getCode())
                .weight(new BigDecimal(dto.getWeight()))
                .volume(new BigDecimal(dto.getVolume()))
                .build();

        return containerRepository.save(container);
    }

    public Container findById(Long id) {
        return containerRepository.findById(id).orElseThrow(() -> new NotFoundException(Deposit.class.getSimpleName(), id));   
    }

    public List<Container> findAll() {
        return containerRepository.findAll();
    }


    @Transactional
    public void deleteById(Long id) {
        Container existing = findById(id);
        containerRepository.delete(existing);
    }
}
