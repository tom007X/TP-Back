package container_transport.gateway.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import container_transport.gateway.dto.AsignTruckDTO;
import container_transport.gateway.dto.ClientDTO;
import container_transport.gateway.dto.CreateShippingRequestDTO;
import container_transport.gateway.dto.ShippingRequestResponseDTO;
import container_transport.gateway.dto.TruckResponseDTO;
import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/shipping-requests")
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

    @GetMapping
    public Mono<ResponseEntity<List<ShippingRequestResponseDTO>>> getAllShippingRequests() {
        return webClientShipping.get()
                .uri("/api/v1/shipping-requests")
                .exchangeToMono(clientResponse -> clientResponse
                        .bodyToFlux(ShippingRequestResponseDTO.class)
                        .collectList()
                        .map(body -> ResponseEntity
                                .status(clientResponse.statusCode())
                                .headers(clientResponse.headers().asHttpHeaders())
                                .body(body)));
    }

    @GetMapping
    public Mono<ResponseEntity<List<ShippingRequestResponseDTO>>> getShippingRequestsByClient(
            @RequestParam Long clientId) {
        return validateClientExists(clientId)
                .then(
                        webClientShipping.get()
                                .uri("/api/v1/shipping-requests" + "?clientId={clientId}", clientId)
                                .exchangeToMono(clientResponse -> clientResponse
                                        .bodyToFlux(ShippingRequestResponseDTO.class)
                                        .collectList()
                                        .map(body -> ResponseEntity
                                                .status(clientResponse.statusCode())
                                                .headers(clientResponse.headers().asHttpHeaders())
                                                .body(body))));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ShippingRequestResponseDTO>> getShippingRequestById(
            @PathVariable Long id, @RequestParam Long clientId) {

        return validateClientExists(clientId)
                .then(
                        webClientShipping.get()
                                .uri("/api/v1/shipping-requests/{id}", id)
                                .exchangeToMono(clientResponse -> clientResponse
                                        .bodyToMono(ShippingRequestResponseDTO.class)
                                        .defaultIfEmpty(null)
                                        .map(body -> ResponseEntity
                                                .status(clientResponse.statusCode())
                                                .headers(clientResponse.headers().asHttpHeaders())
                                                .body(body))));
    }

    @PostMapping
    public Mono<ResponseEntity<ShippingRequestResponseDTO>> createShippingRequest(
            @Valid @RequestBody CreateShippingRequestDTO dto) {
        Mono<ClientDTO> clientMono = validateClientExists(dto.getClientId());

        return clientMono.flatMap(client -> {
            return webClientShipping.post()
                    .uri("/api/v1/shipping-requests")
                    .bodyValue(dto)
                    .exchangeToMono(clientResponse -> {
                        if (clientResponse.statusCode().isError()) {
                            return clientResponse
                                    .bodyToMono(ShippingRequestResponseDTO.class)
                                    .flatMap(errorBody -> Mono.just(
                                            ResponseEntity.status(
                                                    clientResponse.statusCode())
                                                    .headers(clientResponse
                                                            .headers()
                                                            .asHttpHeaders())
                                                    .body(errorBody)));
                        }
                        return clientResponse.bodyToMono(ShippingRequestResponseDTO.class)
                                .map(successBody -> ResponseEntity
                                        .status(clientResponse.statusCode())
                                        .headers(clientResponse.headers()
                                                .asHttpHeaders())
                                        .body(successBody));
                    });
        });
    }

    @PutMapping("/{id}/cancel")
    public Mono<ResponseEntity<ShippingRequestResponseDTO>> cancelShippingRequest(
            @PathVariable Long id,
            @RequestParam Long clientId) {
        return validateClientExists(clientId)
                .then(
                        webClientShipping.put()
                                .uri(uriBuilder -> uriBuilder
                                        .path("/api/v1/shipping-requests/{id}/cancel")
                                        .queryParam("clientId", clientId)
                                        .build(id))
                                .exchangeToMono(clientResponse -> clientResponse
                                        .toEntity(ShippingRequestResponseDTO.class)));

    }

    @PutMapping("/{requestId}/sections/{sectionId}/asign-truck")
    public Mono<ResponseEntity<ShippingRequestResponseDTO>> asignTruckToSection(
            @PathVariable Long requestId,
            @PathVariable Long sectionId,
            @Valid @RequestBody AsignTruckDTO dto) {

        Long truckId = dto.getTruckId();
        if (truckId == null) {
            return Mono.error(new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "TruckId must be provided in AsignTruckDTO"));
        }

        return validateTruckExists(truckId)
                .then(
                        webClientShipping.put()
                                .uri("/api/v1/shipping-requests/{requestId}/sections/{sectionId}/asign-truck",
                                        requestId, sectionId)
                                .bodyValue(dto)
                                .exchangeToMono(clientResponse -> clientResponse
                                        .bodyToMono(ShippingRequestResponseDTO.class)
                                        .map(body -> ResponseEntity
                                                .status(clientResponse
                                                        .statusCode())
                                                .headers(clientResponse
                                                        .headers()
                                                        .asHttpHeaders())
                                                .body(body))));
    }

    private Mono<ClientDTO> validateClientExists(Long clientId) {
        return webClientClient.get()
                .uri("/api/v1/clients/{id}", clientId)
                .retrieve()
                .onStatus(
                        status -> status == HttpStatus.NOT_FOUND,
                        resp -> Mono.error(new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Client with ID " + clientId + " not found.")))
                .bodyToMono(ClientDTO.class);
    }

    private Mono<TruckResponseDTO> validateTruckExists(Long truckId) {
        return webClientTruck.get()
                .uri("/api/v1/trucks/{id}", truckId)
                .retrieve()
                .onStatus(
                        status -> status == HttpStatus.NOT_FOUND,
                        resp -> Mono.error(new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Truck with ID " + truckId + " not found.")))
                .bodyToMono(TruckResponseDTO.class);
    }
}