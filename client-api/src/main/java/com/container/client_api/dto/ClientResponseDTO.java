package com.container.client_api.dto;

import com.container.client_api.models.Client;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientResponseDTO {

    private Long id;
    private String name;
    private String surname;
    private String phone;
    private String address;

    public static ClientResponseDTO toDTO(Client clientEntity){
        return ClientResponseDTO.builder()
                .id(clientEntity.getId())
                .name(clientEntity.getName())
                .surname(clientEntity.getSurname())
                .phone(clientEntity.getPhone())
                .address(clientEntity.getAddress())
                .build();
    }
}
