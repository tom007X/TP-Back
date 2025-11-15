package com.container.client_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ClientRequestDTO {

    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúñÑ ]+$",
            message = "El nombre solo puede contener letras y espacios.")
    @NotBlank(message = "Name is required")
    private String name;

    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúñÑ ]+$",
            message = "El nombre solo puede contener letras y espacios.")
    @NotBlank(message = "Surname is required")
    private String surname;

    @Pattern( regexp = "^\\+54\\s?9?\\s?[0-9]{2,4}\\s?[0-9]{6,8}$",
            message = "Invalid Argentine phone number format"
    )
    @NotBlank(message = "Phone is required")
    private String phone;

    private String address;
}
