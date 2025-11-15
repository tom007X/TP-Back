package com.example.TruckMicroservice.dto;

import com.example.TruckMicroservice.model.Driver;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DriverRequestDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Surname is required")
    private String surname;

    @NotBlank(message = "Phone is required")
    private String phone;

    public Driver toEntity(){
        Driver driver = new Driver();
        driver.setName(this.name);
        driver.setSurname(this.surname);
        driver.setPhone(this.phone);
        return driver;
    }

}
