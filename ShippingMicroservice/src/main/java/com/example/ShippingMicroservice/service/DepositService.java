package com.example.ShippingMicroservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ShippingMicroservice.dto.AddressRequestDTO;
import com.example.ShippingMicroservice.dto.AddressResponseDTO;
import com.example.ShippingMicroservice.dto.DepositRequestDTO;
import com.example.ShippingMicroservice.dto.DepositResponseDTO;
import com.example.ShippingMicroservice.exception.BadRequestException;
import com.example.ShippingMicroservice.exception.NotFoundException;
import com.example.ShippingMicroservice.model.Address;
import com.example.ShippingMicroservice.model.Deposit;
import com.example.ShippingMicroservice.repository.AddressRepository;
import com.example.ShippingMicroservice.repository.DepositRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepositService {

    private final DepositRepository depositRepository;
    private final AddressRepository addressRepository;
    private final AddressService addressService;

    @Transactional
    public DepositResponseDTO create(DepositRequestDTO dto) throws BadRequestException {
        Address address = addressFromDTO(dto);

        Deposit newDeposit = Deposit.builder()
                .name(dto.getName())
                .address(address)
                .dailyStorageCost(dto.getDailyStorageCost())
                .build();

        Deposit deposit =  depositRepository.save(newDeposit);
        return DepositResponseDTO.fromEntity(deposit);
    }

    public DepositResponseDTO update(Long id, DepositRequestDTO dto) throws BadRequestException {
        Deposit current = findEntityById(id);
        if (dto.getName() != null)
            current.setName(dto.getName());
        if (dto.getDailyStorageCost() != null)
            current.setDailyStorageCost(dto.getDailyStorageCost());
        Address address = addressFromDTO(dto);
        current.setAddress(address);
        Deposit deposit =  depositRepository.save(current);
        return DepositResponseDTO.fromEntity(deposit);
    }

    public DepositResponseDTO findById(Long id) {
        Deposit deposit = depositRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Deposit.class.getSimpleName(), id));
        return DepositResponseDTO.fromEntity(deposit);

    }

    public List<DepositResponseDTO> findAll() {
        List<Deposit> deposits = depositRepository.findAll();
        return deposits.stream()
                .map(DepositResponseDTO::fromEntity)
                .toList();
    }

    @Transactional
    public void deleteById(Long id) {
        Deposit existing = findEntityById(id);
        depositRepository.delete(existing);
    }

    public Deposit findEntityById(Long id) {
        return depositRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Deposit.class.getSimpleName(), id));
    }

    private Address addressFromDTO(DepositRequestDTO dto) {
        if (dto.getAddressId() != null) {
            return addressRepository.findById(dto.getAddressId())
                    .orElseThrow(
                            () -> new BadRequestException("Address with id " + dto.getAddressId() + " does not exist"));
        }
        AddressRequestDTO addrDto = dto.getAddress();
        return AddressResponseDTO.toEntity(addressService.create(addrDto));
    }

}
