package container_transport.gateway.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import container_transport.gateway.dto.ClientDTO;
import container_transport.gateway.dto.CreateShippingRequestDTO;
import container_transport.gateway.dto.ShippingRequestResponseDTO;
import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/composite/shipping-requests")
public class ShippingCompositionController {
    private final WebClient webClientShipping;
    private final WebClient webClientTruck;
    private final WebClient webClientClient;

    public ShippingCompositionController(
            WebClient.Builder webClientBuilder,
            @Value("http://localhost:8090") String uriShipping,
            @Value("http://localhost:8091") String uriTruck,
            @Value("http://localhost:8092") String uriClient) {

        this.webClientShipping = webClientBuilder.baseUrl(uriShipping).build();
        this.webClientTruck = webClientBuilder.baseUrl(uriTruck).build();
        this.webClientClient = webClientBuilder.baseUrl(uriClient).build();
    }

    @PostMapping
    public Mono<ResponseEntity<ShippingRequestResponseDTO>> createShippingRequest(
            @Valid @RequestBody CreateShippingRequestDTO dto) {
        Mono<ClientDTO> clientMono = webClientClient.get()
                .uri("/api/v1/clients/{id}", dto.getClientId())
                .retrieve()
                .onStatus(s -> s == HttpStatus.NOT_FOUND,
                        clientResponse -> Mono.error(new ResponseStatusException(
                                HttpStatus.BAD_REQUEST, "Client with ID " + dto.getClientId() + " not found.")))
                .bodyToMono(ClientDTO.class);

        return clientMono.flatMap(client -> {
            return webClientShipping.post()
                    .uri("/api/v1/shipping-requests")
                    .bodyValue(dto)
                    .exchangeToMono(clientResponse -> {
                        if (clientResponse.statusCode().isError()) {
                            return clientResponse.bodyToMono(ShippingRequestResponseDTO.class)
                                    .flatMap(errorBody -> Mono.just(
                                            ResponseEntity.status(clientResponse.statusCode())
                                                    .headers(clientResponse.headers().asHttpHeaders())
                                                    .body(errorBody)));
                        }
                        return clientResponse.bodyToMono(ShippingRequestResponseDTO.class)
                                .map(successBody -> ResponseEntity.status(clientResponse.statusCode())
                                        .headers(clientResponse.headers().asHttpHeaders())
                                        .body(successBody));
                    });
        });
    }
}