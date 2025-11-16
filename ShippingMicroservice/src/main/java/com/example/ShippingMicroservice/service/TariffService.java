package com.example.ShippingMicroservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ShippingMicroservice.dto.tariff.TariffAvailabilityRequestDTO;
import com.example.ShippingMicroservice.dto.tariff.TariffDTO;
import com.example.ShippingMicroservice.dto.tariff.TariffRequestDTO;
import com.example.ShippingMicroservice.exception.NotFoundException;
import com.example.ShippingMicroservice.model.Tariff;
import com.example.ShippingMicroservice.repository.TariffRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TariffService {

    private final TariffRepository tariffRepository;

    @Transactional
    public TariffDTO create(TariffRequestDTO dto) {
        Tariff tariff = Tariff.builder()
                .subject(dto.getSubject())
                .metric(dto.getMetric())
                .value(dto.getValue())
                .available(true) // default to enabled when created
                .build();

        return TariffDTO.fromEntity(tariffRepository.save(tariff));
    }

    public List<TariffDTO> findAll() {
        return tariffRepository.findAll().stream()
                .map(TariffDTO::fromEntity)
                .toList();
    }

    public TariffDTO findById(Long id) {
        Tariff tariff = tariffRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tariff with id " + id + " not found"));
        return TariffDTO.fromEntity(tariff);
    }


    @Transactional
    public TariffDTO updateValueBySubjectAndMetric(TariffRequestDTO dto) {
        Tariff tariff = tariffRepository
                .findBySubjectAndMetric(dto.getSubject(), dto.getMetric())
                .orElseThrow(() -> new NotFoundException(
                        "Tariff with subject '%s' and metric '%s' not found"
                                .formatted(dto.getSubject(), dto.getMetric())
                ));

        tariff.setValue(dto.getValue());
        return TariffDTO.fromEntity(tariffRepository.save(tariff));
    }

    @Transactional
    public TariffDTO updateAvailability(TariffAvailabilityRequestDTO dto) {
        Tariff tariff = tariffRepository
                .findBySubjectAndMetric(dto.getSubject(), dto.getMetric())
                .orElseThrow(() -> new NotFoundException("Tariff with subject '%s' and metric '%s' not found"
                                .formatted(dto.getSubject(), dto.getMetric())));

        tariff.setAvailable(dto.getAvailable());
        return TariffDTO.fromEntity(tariffRepository.save(tariff));
    }

    @Transactional
    public void deleteById(Long id) {
        Tariff existing = tariffRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tariff with id " + id + " not found"));
        tariffRepository.delete(existing);
    }
}
