package com.container.client_api.controllers;

import com.container.client_api.models.Client;
import com.container.client_api.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping()
    public ResponseEntity<List<Client>> getAllClient(){
        List<Client> client = clientService.getAll();
        if (client.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(client);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id){
        Client client = clientService.getById(id);
        if (client == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(client);
    }

    @PostMapping()
    public ResponseEntity<Client> createClient(@Valid @RequestBody Client client){
        Client clientCreated = clientService.save(client);
        return ResponseEntity.status(HttpStatus.CREATED).body(clientCreated);
    }

}
