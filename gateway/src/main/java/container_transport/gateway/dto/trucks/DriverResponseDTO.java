package container_transport.gateway.dto.trucks;

import lombok.Data;

@Data
public class DriverResponseDTO {
    private Long id;
    private String name;
    private String surname;
    private String phone;
}
