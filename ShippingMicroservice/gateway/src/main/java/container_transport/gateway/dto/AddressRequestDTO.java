package container_transport.gateway.dto;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AddressRequestDTO {

    @NotEmpty
    private String city;
    @NotEmpty
    private String postalCode;

    @NotEmpty
    private String street;
    private String number;

    @Min(-90)
    @Max(90)
    private Double latitude;
    @Min(-180)
    @Max(180)
    private Double longitude;

}
