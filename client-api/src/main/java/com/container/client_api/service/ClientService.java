package com.container.client_api.service;

import com.container.client_api.models.Client;
import com.container.client_api.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public List<Client> getAll(){ return clientRepository.findAll(); }

    public Client getById(Long id){
        return clientRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public Client save(Client client){ return clientRepository.save(client);}

}
