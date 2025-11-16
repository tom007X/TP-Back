package com.example.ShippingMicroservice.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ShippingMicroservice.dto.container.ContainerDTO;
import com.example.ShippingMicroservice.dto.container.ContainerRequestDTO;
import com.example.ShippingMicroservice.exception.NotFoundException;
import com.example.ShippingMicroservice.model.Container;
import com.example.ShippingMicroservice.model.ContainerStatus;
import com.example.ShippingMicroservice.model.Deposit;
import com.example.ShippingMicroservice.repository.ContainerRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContainerService {
    private final ContainerRepository containerRepository;

    @Transactional
    public Container create(ContainerRequestDTO dto) {
        Container container = Container.builder()
                .code(dto.getCode())
                .weight(new BigDecimal(dto.getWeight()))
                .volume(new BigDecimal(dto.getVolume()))
                .status(ContainerStatus.LIBRE)
                .build();

        return containerRepository.save(container);
    }

    @Transactional
    public Container create(ContainerRequestDTO dto, ContainerStatus status) {
        Container container = Container.builder()
                .code(dto.getCode())
                .weight(new BigDecimal(dto.getWeight()))
                .volume(new BigDecimal(dto.getVolume()))
                .status(status)
                .build();

        return containerRepository.save(container);
    }

    public Container findById(Long id) {
        return containerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Deposit.class.getSimpleName(), id));
    }

    public List<ContainerDTO> findAll() {
        return containerRepository.findAll()
                .stream()
                .map(ContainerDTO::fromEntity)
                .toList();
    }

    public List<ContainerDTO> findByStatus(ContainerStatus status) {
        return containerRepository.findByStatus(status)
                .stream()
                .map(ContainerDTO::fromEntity)
                .toList();
    }

    public void asignate(String code, ContainerStatus status) {
        Container container = containerRepository.findByCode(code)
                .orElseThrow(() -> new NotFoundException("Container with code " + code + " not found"));
        container.setStatus(status);
        containerRepository.save(container);
    }

    @Transactional
    public void deleteById(Long id) {
        Container existing = findById(id);
        containerRepository.delete(existing);
    }
}
