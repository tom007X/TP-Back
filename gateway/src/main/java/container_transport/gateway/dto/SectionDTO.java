package container_transport.gateway.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SectionDTO {
    private Long id;
    private LocalDateTime dateTimeStart;
    private LocalDateTime dateTimeEnd;
    private Double distance;
    private String assignatedTruck;
    private String start;
    private String end;
    private String status;
    private String type;

}
