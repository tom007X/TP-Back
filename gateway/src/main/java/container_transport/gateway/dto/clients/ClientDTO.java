package container_transport.gateway.dto.clients;

import lombok.Data;

@Data
public class ClientDTO {
    private Long id;
    private String name;
    private String surname;
    private String phone;
    private String address;
}
