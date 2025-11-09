package com.example.ShippingMicroservice.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.ShippingMicroservice.dto.AddressRequestDTO;
import com.example.ShippingMicroservice.dto.CreateShippingRequestDTO;
import com.example.ShippingMicroservice.dto.RouteDTO;
import com.example.ShippingMicroservice.dto.ShippingRequestResponseDTO;
import com.example.ShippingMicroservice.exception.BadRequestException;
import com.example.ShippingMicroservice.model.Address;
import com.example.ShippingMicroservice.model.Container;
import com.example.ShippingMicroservice.model.Route;
import com.example.ShippingMicroservice.model.ShippingRequest;
import com.example.ShippingMicroservice.model.ShippingRequestStatus;
import com.example.ShippingMicroservice.repository.AddressRepository;
import com.example.ShippingMicroservice.repository.ContainerRepository;
import com.example.ShippingMicroservice.repository.ShippingRequestRepository;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShippingRequestService {

    private final ShippingRequestRepository shippingRequestRepository;
    private final ContainerRepository containerRepository;
    private final AddressRepository addressRepository;
    private final RouteCreationService routeCreationService;
    private final CostCalculationService costCalculationService;

    @Transactional
    public ShippingRequestResponseDTO createShippingRequest(CreateShippingRequestDTO dto) {
        // Validate container exists and is available
        Container container = containerRepository.findById(dto.getContainerId())
                .orElseThrow(() -> new BadRequestException(
                        "Container not found with ID: " + dto.getContainerId()));

        if (!isContainerAvailable(container)) {
            throw new IllegalStateException("Container is not available for shipping");
        }

        // Create or find addresses
        Address startAddress = createOrFindAddress(dto.getStartAddress());
        Address endAddress = createOrFindAddress(dto.getEndAddress());

        // Create optimized route with deposits
        Route route = routeCreationService.createRouteWithDeposits(startAddress, endAddress);

        // Calculate estimated cost and time
        BigDecimal estimatedCost = costCalculationService.calculateCost(route, container);
        String estimatedTime = costCalculationService.calculateEstimatedTime(route);

        // Create shipping request
        ShippingRequest shippingRequest = new ShippingRequest();
        shippingRequest.setRequestDatetime(LocalDateTime.now());
        shippingRequest.setEstimatedCost(estimatedCost);
        shippingRequest.setEstimatedTime(estimatedTime);
        shippingRequest.setStatus(ShippingRequestStatus.ESTIMADO);
        shippingRequest.setContainer(container);
        shippingRequest.setClientId(dto.getClientId());

        ShippingRequest saved = shippingRequestRepository.save(shippingRequest);

        return mapToResponseDTO(saved, route);
    }

    /**
     * Gets a shipping request by ID
     */
    @Transactional(readOnly = true)
    public ShippingRequestResponseDTO getShippingRequest(Long id, Long clientId) {
        ShippingRequest request = shippingRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Shipping request not found with ID: " + id));

        // Verify the request belongs to the client
        if (!request.getClientId().equals(clientId)) {
            throw new SecurityException("Access denied to this shipping request");
        }

        return mapToResponseDTO(request, null);
    }

    /**
     * Gets all shipping requests for a client
     */
    @Transactional(readOnly = true)
    public List<ShippingRequestResponseDTO> getShippingRequestsByClient(Long clientId) {
        List<ShippingRequest> requests = shippingRequestRepository
                .findByClientIdOrderByRequestDatetimeDesc(clientId);

        return requests.stream()
                .map(request -> mapToResponseDTO(request, null))
                .collect(Collectors.toList());
    }

    /**
     * Cancels a shipping request
     */
    @Transactional
    public ShippingRequestResponseDTO cancelShippingRequest(Long id, Long clientId) {
        ShippingRequest request = shippingRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Shipping request not found with ID: " + id));

        // Verify the request belongs to the client
        if (!request.getClientId().equals(clientId)) {
            throw new SecurityException("Access denied to this shipping request");
        }

        // Check if request can be cancelled
        if (request.getStatus() == ShippingRequestStatus.FINALIZADO ||
                request.getStatus() == ShippingRequestStatus.CANCELADO) {
            throw new IllegalStateException(
                    "Cannot cancel a request with status: " + request.getStatus());
        }

        request.setStatus(ShippingRequestStatus.CANCELADO);
        ShippingRequest updated = shippingRequestRepository.save(request);

        return mapToResponseDTO(updated, null);
    }

    /**
     * Creates or finds an existing address
     */
    private Address createOrFindAddress(AddressRequestDTO dto) {
        Address existing = addressRepository.findByLatitudeAndLongitude(
                dto.getLatitude(), dto.getLongitude());

        if (existing != null)
            return existing;

        // Create new address
        Address address = AddressRequestDTO.toEntity(dto);
        return addressRepository.save(address);
    }

    /**
     * Checks if container is available for shipping
     */
    private boolean isContainerAvailable(Container container) {
        // Add your business logic here
        // For example: check if container is not already assigned to another active
        // request
        return true;
    }

    /**
     * Maps ShippingRequest entity to response DTO
     */
    private ShippingRequestResponseDTO mapToResponseDTO(ShippingRequest request, Route route) {
        RouteDTO routeDTO = RouteDTO.builder()
                .id(route.getId())
                .numDeposits(route.getNumDeposit())
                .numSections(route.getNumSections())
                .build();
        ShippingRequestResponseDTO dto = ShippingRequestResponseDTO.builder()
                .id(request.getId())
                .requestDatetime(request.getRequestDatetime())
                .estimatedCost(request.getEstimatedCost())
                .estimatedTime(request.getEstimatedTime())
                .finalCost(request.getFinalCost())
                .realTime(request.getRealTime())
                .status(request.getStatus())
                .containerId(request.getContainer() != null ? request.getContainer().getId() : null)
                .clientId(request.getClientId())
                .route(routeDTO)
                .build();
        return dto;
    }
}
