package com.container.client_api.service;

import com.container.client_api.dto.ClientRequestDTO;
import com.container.client_api.dto.ClientResponseDTO;
import com.container.client_api.exception.NotFoundException;
import com.container.client_api.models.Client;
import com.container.client_api.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository repositoryImp;

    @Transactional(readOnly = true)
    public List<ClientResponseDTO> getAllClients() {
        return repositoryImp.findAll()
                .stream()
                .map(ClientResponseDTO::toDTO)
                .toList();
    }


    @Transactional(readOnly = true)
    public ClientResponseDTO findClientById(Long id) {
        Client findClient = repositoryImp.findById(id)
                .orElseThrow(() -> new NotFoundException(Client.class.getSimpleName(), id));
        return ClientResponseDTO.toDTO(findClient);
    }


    @Transactional
    public ClientResponseDTO createClientDTO(ClientRequestDTO clientRequestDTO) {

        Client newClient = Client.builder()
                .name(clientRequestDTO.getName())
                .surname(clientRequestDTO.getSurname())
                .phone(clientRequestDTO.getPhone())
                .address((clientRequestDTO.getAddress()))
                .build();

        repositoryImp.save(newClient);
        return ClientResponseDTO.toDTO(newClient);
    }


}
