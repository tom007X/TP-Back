package com.example.ShippingMicroservice.service;

import java.util.Collections;
import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import com.example.ShippingMicroservice.dto.AddressRequestDTO;
import com.example.ShippingMicroservice.dto.DepositRequestDTO;
import com.example.ShippingMicroservice.exception.NotFoundException;
import com.example.ShippingMicroservice.model.Address;
import com.example.ShippingMicroservice.model.Deposit;
import com.example.ShippingMicroservice.repository.AddressRepositoryImpl;
import com.example.ShippingMicroservice.repository.DepositRepositoryImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepositService {

    private final DepositRepositoryImpl depositRepository;
    private final AddressRepositoryImpl addressRepository;
    private final AddressService addressService;

    @Transactional
    public Deposit create(DepositRequestDTO dto) throws BadRequestException{
        Address address;
        if (dto.getAddressId() != null) {
            address = addressRepository.findById(dto.getAddressId())
                    .orElseThrow(() -> new BadRequestException("Address with id " + dto.getAddressId() + " does not exist"));
        } else {
            AddressRequestDTO addrDto = dto.getAddress();
            address = addressService.create(addrDto);
        }

        Deposit deposit = Deposit.builder()
                .name(dto.getName())
                .address(address)
                .build();

        return depositRepository.save(deposit);
    }

    public Deposit findById(Long id) {
        return depositRepository.findById(id).orElseThrow(() -> new NotFoundException(Deposit.class.getSimpleName(), id));
        
    }

    public List<Deposit> findAll() {
        return depositRepository.findAll();
    }

    @Transactional
    public void deleteById(Long id) {
        Deposit existing = findById(id);
        depositRepository.delete(existing);
    }
}
