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
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import container_transport.gateway.dto.clients.ClientDTO;
import container_transport.gateway.dto.shipping_requests.AsignTruckDTO;
import container_transport.gateway.dto.shipping_requests.CreateShippingRequestDTO;
import container_transport.gateway.dto.shipping_requests.ShippingRequestResponseDTO;
import container_transport.gateway.dto.trucks.TruckResponseDTO;
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
            @Value("${routing.microservice.shipping:http://shipping-microservice:8090}") String uriShipping,
            @Value("${routing.microservice.trucks:http://truck-microservice:8091}") String uriTruck,
            @Value("${routing.microservice.clients:http://client-microservice:8092}") String uriClient) {

        this.webClientShipping = webClientBuilder.baseUrl(uriShipping).build();
        this.webClientTruck = webClientBuilder.baseUrl(uriTruck).build();
        this.webClientClient = webClientBuilder.baseUrl(uriClient).build();
    }

    @GetMapping
    public Mono<ResponseEntity<List<ShippingRequestResponseDTO>>> getShippingRequestsByClient(
            @RequestParam Long clientId) {
        return validateClientExists(clientId)
                .then(webClientShipping.get()
                        .uri("/api/v1/shipping-requests?clientId={clientId}", clientId)
                        .retrieve()
                        .onStatus(
                                status -> status.is4xxClientError() || status.is5xxServerError(),
                                response -> handleErrorResponse(response, "fetching shipping requests"))
                        .toEntityList(ShippingRequestResponseDTO.class))
                .onErrorResume(this::handleError);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ShippingRequestResponseDTO>> getShippingRequestById(
            @PathVariable Long id, @RequestParam Long clientId) {

        return validateClientExists(clientId)
                .then(webClientShipping.get()
                        .uri("/api/v1/shipping-requests/{id}", id)
                        .retrieve()
                        .onStatus(
                                status -> status == HttpStatus.NOT_FOUND,
                                resp -> Mono.error(new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Shipping request with ID " + id + " not found.")))
                        .onStatus(
                                status -> status.is4xxClientError() || status.is5xxServerError(),
                                response -> handleErrorResponse(response, "fetching shipping request"))
                        .toEntity(ShippingRequestResponseDTO.class))
                .onErrorResume(this::handleError);
    }

    @GetMapping("/container/{containerCode}")
    public Mono<ResponseEntity<ShippingRequestResponseDTO>> getShippingRequestByContainerCode(
            @PathVariable String containerCode,
            @RequestParam Long clientId) {

        return validateClientExists(clientId)
                .then(webClientShipping.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/api/v1/shipping-requests/container/{containerCode}")
                                .queryParam("clientId", clientId)
                                .build(containerCode))
                        .retrieve()
                        .onStatus(
                                status -> status == HttpStatus.NOT_FOUND,
                                resp -> Mono.error(new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Shipping request with container code " + containerCode + " not found.")))
                        .onStatus(
                                status -> status.is4xxClientError() || status.is5xxServerError(),
                                response -> handleErrorResponse(response, "fetching shipping request by container"))
                        .toEntity(ShippingRequestResponseDTO.class))
                .onErrorResume(this::handleError);
    }

    @PostMapping
    public Mono<ResponseEntity<ShippingRequestResponseDTO>> createShippingRequest(
            @Valid @RequestBody CreateShippingRequestDTO dto) {
        
        return validateClientExists(dto.getClientId())
                .flatMap(client -> webClientShipping.post()
                        .uri("/api/v1/shipping-requests")
                        .bodyValue(dto)
                        .retrieve()
                        .onStatus(
                                status -> status == HttpStatus.BAD_REQUEST,
                                response -> handleErrorResponse(response, "creating shipping request"))
                        .onStatus(
                                status -> status.is4xxClientError() || status.is5xxServerError(),
                                response -> handleErrorResponse(response, "creating shipping request"))
                        .toEntity(ShippingRequestResponseDTO.class))
                .onErrorResume(this::handleError);
    }

    @PutMapping("/{id}/cancel")
    public Mono<ResponseEntity<ShippingRequestResponseDTO>> cancelShippingRequest(
            @PathVariable Long id,
            @RequestParam Long clientId) {
        
        return validateClientExists(clientId)
                .then(webClientShipping.put()
                        .uri(uriBuilder -> uriBuilder
                                .path("/api/v1/shipping-requests/{id}/cancel")
                                .queryParam("clientId", clientId)
                                .build(id))
                        .retrieve()
                        .onStatus(
                                status -> status == HttpStatus.NOT_FOUND,
                                resp -> Mono.error(new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Shipping request with ID " + id + " not found.")))
                        .onStatus(
                                status -> status == HttpStatus.BAD_REQUEST,
                                response -> handleErrorResponse(response, "canceling shipping request"))
                        .onStatus(
                                status -> status.is4xxClientError() || status.is5xxServerError(),
                                response -> handleErrorResponse(response, "canceling shipping request"))
                        .toEntity(ShippingRequestResponseDTO.class))
                .onErrorResume(this::handleError);
    }

    @PutMapping("/{requestId}/sections/{sectionId}/asign-truck/{truckId}")
    public Mono<ResponseEntity<ShippingRequestResponseDTO>> asignTruckToSection(
            @PathVariable Long requestId,
            @PathVariable Long sectionId,
            @PathVariable Long truckId) {

        return validateTruckExists(truckId)
                .flatMap(truckDto -> {
                    AsignTruckDTO asignTruckDTO = new AsignTruckDTO();
                    asignTruckDTO.setTruckId(truckId);
                    asignTruckDTO.setTruckPlate(truckDto.getLicensePlate());
                    asignTruckDTO.setTruckCostPerKm(truckDto.getCostPerKm());

                    Mono<Void> markTruckUnavailable = webClientTruck.patch()
                            .uri(uriBuilder -> uriBuilder
                                    .path("/api/v1/trucks/{id}/availability")
                                    .queryParam("available", false)
                                    .build(truckId))
                            .retrieve()
                            .onStatus(
                                    status -> status.is4xxClientError() || status.is5xxServerError(),
                                    response -> handleErrorResponse(response, "updating truck availability"))
                            .toBodilessEntity()
                            .then();

                    return markTruckUnavailable.then(
                            webClientShipping.put()
                                    .uri(uriBuilder -> uriBuilder
                                            .path("/api/v1/shipping-requests/{requestId}/sections/{sectionId}/asign-truck")
                                            .build(requestId, sectionId))
                                    .bodyValue(asignTruckDTO)
                                    .retrieve()
                                    .onStatus(
                                            status -> status == HttpStatus.BAD_REQUEST,
                                            response -> handleErrorResponse(response, "assigning truck to section"))
                                    .onStatus(
                                            status -> status.is4xxClientError() || status.is5xxServerError(),
                                            response -> handleErrorResponse(response, "assigning truck to section"))
                                    .toEntity(ShippingRequestResponseDTO.class));
                })
                .onErrorResume(this::handleError);
    }

    @PutMapping("/{requestId}/sections/{sectionId}/start")
    public Mono<ResponseEntity<ShippingRequestResponseDTO>> startSection(
            @PathVariable Long requestId,
            @PathVariable Long sectionId) {

        return webClientShipping.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/shipping-requests/{requestId}/sections/{sectionId}/start")
                        .build(requestId, sectionId))
                .retrieve()
                .onStatus(
                        status -> status == HttpStatus.BAD_REQUEST,
                        response -> handleErrorResponse(response, "starting section"))
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> handleErrorResponse(response, "starting section"))
                .toEntity(ShippingRequestResponseDTO.class)
                .onErrorResume(this::handleError);
    }

    @PutMapping("/{requestId}/sections/{sectionId}/finish")
    public Mono<ResponseEntity<ShippingRequestResponseDTO>> finishSection(
            @PathVariable Long requestId,
            @PathVariable Long sectionId) {

        return webClientShipping.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/shipping-requests/{requestId}/sections/{sectionId}/finish")
                        .build(requestId, sectionId))
                .retrieve()
                .onStatus(
                        status -> status == HttpStatus.BAD_REQUEST,
                        response -> handleErrorResponse(response, "finishing section"))
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> handleErrorResponse(response, "finishing section"))
                .toEntity(ShippingRequestResponseDTO.class)
                .onErrorResume(this::handleError);
    }

    // ========== HELPER METHODS ==========

    private Mono<ClientDTO> validateClientExists(Long clientId) {
        return webClientClient.get()
                .uri("/api/v1/clients/{id}", clientId)
                .retrieve()
                .onStatus(
                        status -> status == HttpStatus.NOT_FOUND,
                        resp -> Mono.error(new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Client with ID " + clientId + " not found.")))
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> handleErrorResponse(response, "validating client"))
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
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> handleErrorResponse(response, "validating truck"))
                .bodyToMono(TruckResponseDTO.class);
    }

    /**
     * Handles error responses from downstream microservices
     */
    private Mono<Throwable> handleErrorResponse(
            ClientResponse response,
            String operation) {
        
       return response.bodyToMono(String.class)
                .map(errorBody -> {
                    HttpStatus status = (HttpStatus) response.statusCode();
                    String message = String.format("Error %s: %s - %s", 
                            operation, 
                            status.value(), 
                            errorBody);
                    
                    return (Throwable) new ResponseStatusException(status, message);
                })
                .defaultIfEmpty((Throwable) new ResponseStatusException(
                        (HttpStatus) response.statusCode(),
                        "Error " + operation + ": " + response.statusCode()))
                .flatMap(Mono::error);
    }

    /**
     * Generic error handler that converts various exceptions to appropriate HTTP responses
     */
    private <T> Mono<ResponseEntity<T>> handleError(Throwable error) {
        if (error instanceof ResponseStatusException) {
            ResponseStatusException rse = (ResponseStatusException) error;
            return Mono.just(ResponseEntity
                    .status(rse.getStatusCode())
                    .body(null));
        }
        
        if (error instanceof WebClientResponseException) {
            WebClientResponseException wcre = (WebClientResponseException) error;
            return Mono.just(ResponseEntity
                    .status(wcre.getStatusCode())
                    .body(null));
        }
        
        // Default to 500 Internal Server Error for unexpected exceptions
        return Mono.just(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(null));
    }
}