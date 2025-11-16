package com.example.TruckMicroservice.dto;

import com.example.TruckMicroservice.model.Driver;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DriverResponseDTO {

    private Long id;
    private String name;
    private String surname;
    private String phone;

    public static DriverResponseDTO toDTO(Driver driverEntity){
        return DriverResponseDTO.builder()
                .id(driverEntity.getId())
                .name(driverEntity.getName())
                .surname(driverEntity.getSurname())
                .phone(driverEntity.getPhone())
                .build();
    }

    public Driver toEntity(){
        Driver driver = new Driver();
        driver.setId(this.id);
        driver.setName(this.name);
        driver.setSurname(this.surname);
        driver.setPhone(this.phone);
        return driver;
    }

}
