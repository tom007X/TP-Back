package com.example.ShippingMicroservice.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.ShippingMicroservice.dto.AddressRequestDTO;
import com.example.ShippingMicroservice.dto.AsignTruckDTO;
import com.example.ShippingMicroservice.dto.CreateShippingRequestDTO;
import com.example.ShippingMicroservice.dto.ShippingRequestResponseDTO;
import com.example.ShippingMicroservice.exception.BadRequestException;
import com.example.ShippingMicroservice.exception.NotFoundException;
import com.example.ShippingMicroservice.model.Address;
import com.example.ShippingMicroservice.model.Container;
import com.example.ShippingMicroservice.model.ContainerStatus;
import com.example.ShippingMicroservice.model.Route;
import com.example.ShippingMicroservice.model.SectionStatus;
import com.example.ShippingMicroservice.model.ShippingRequest;
import com.example.ShippingMicroservice.model.ShippingRequestStatus;
import com.example.ShippingMicroservice.repository.AddressRepository;
import com.example.ShippingMicroservice.repository.ContainerRepository;
import com.example.ShippingMicroservice.repository.RouteRepository;
import com.example.ShippingMicroservice.repository.SectionRepository;
import com.example.ShippingMicroservice.repository.ShippingRequestRepository;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShippingRequestService {

    private final ShippingRequestRepository shippingRequestRepository;
    private final ContainerRepository containerRepository;
    private final AddressRepository addressRepository;
    private final RouteRepository routeRepository;
    private final SectionRepository sectionRepository;
    private final RouteCreationService routeCreationService;
    private final CostCalculationService costCalculationService;

    @Transactional
    public ShippingRequestResponseDTO createShippingRequest(CreateShippingRequestDTO dto) {
        Container container = containerRepository.findById(dto.getContainerId())
                .orElseThrow(() -> new BadRequestException(
                        "Container not found with ID: " + dto.getContainerId()));

        if (!isContainerAvailable(container)) {
            throw new BadRequestException("Container is not available for shipping");
        }

        Address startAddress = createOrFindAddress(dto.getStartAddress());
        Address endAddress = createOrFindAddress(dto.getEndAddress());

        Route route = routeCreationService.createRouteWithDeposits(startAddress, endAddress);

        BigDecimal estimatedCost = costCalculationService.estimateCost(route);
        String estimatedTime = costCalculationService.calculateEstimatedTime(route);

        ShippingRequest shippingRequest = new ShippingRequest();
        shippingRequest.setRequestDatetime(LocalDateTime.now());
        shippingRequest.setEstimatedCost(estimatedCost);
        shippingRequest.setEstimatedTime(estimatedTime);
        shippingRequest.setStatus(ShippingRequestStatus.PENDIENTE);
        shippingRequest.setContainer(container);
        shippingRequest.setClientId(dto.getClientId());

        ShippingRequest saved = shippingRequestRepository.save(shippingRequest);
        route.setRequest(saved);
        Route savedRoute = routeRepository.save(route);
        savedRoute.getSections().forEach(section -> {
            section.setRoute(savedRoute);
            sectionRepository.save(section);
        });
        saved.setRoute(savedRoute);

        return ShippingRequestResponseDTO.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public ShippingRequestResponseDTO getShippingRequest(Long id, Long clientId) {
        ShippingRequest request = shippingRequestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("ShippingRequest", id));

        if (!request.getClientId().equals(clientId)) {
            throw new SecurityException("Access denied to this shipping request");
        }

        return ShippingRequestResponseDTO.fromEntity(request);
    }

    @Transactional(readOnly = true)
    public List<ShippingRequestResponseDTO> getShippingRequestsByClient(Long clientId) {
        List<ShippingRequest> requests = shippingRequestRepository
                .findByClientIdOrderByRequestDatetimeDesc(clientId);

        return requests.stream()
                .map(request -> ShippingRequestResponseDTO.fromEntity(request))
                .collect(Collectors.toList());
    }

    @Transactional
    public ShippingRequestResponseDTO cancelShippingRequest(Long id, Long clientId) {
        ShippingRequest request = shippingRequestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("ShippingRequest", id));

        if (!request.getClientId().equals(clientId)) {
            throw new SecurityException("Access denied to this shipping request");
        }

        if (request.getStatus() == ShippingRequestStatus.FINALIZADO ||
                request.getStatus() == ShippingRequestStatus.CANCELADO) {
            throw new IllegalStateException(
                    "Cannot cancel a request with status: " + request.getStatus());
        }

        request.setStatus(ShippingRequestStatus.CANCELADO);
        ShippingRequest updated = shippingRequestRepository.save(request);

        return ShippingRequestResponseDTO.fromEntity(updated);
    }

    @Transactional
    public ShippingRequestResponseDTO asignTruckToSection(Long requestId, Long sectionId, AsignTruckDTO dto) {
        ShippingRequest request = shippingRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("ShippingRequest", requestId));

        Route route = request.getRoute();
        if (route == null) {
            throw new NotFoundException("Route for ShippingRequest", requestId);
        }

        var sectionOpt = route.getSections().stream()
                .filter(s -> s.getId().equals(sectionId))
                .findFirst();

        if (sectionOpt.isEmpty()) {
            throw new NotFoundException("Section", sectionId);
        }

        var section = sectionOpt.get();
        section.setTruckId(dto.getTruckId());
        section.setRealCost(BigDecimal.valueOf(dto.getTruckCostPerKm())
                .multiply(BigDecimal.valueOf(section.getDistance())));
        section.setStatus(SectionStatus.ASIGNADO);

        BigDecimal estimatedCost = costCalculationService.estimateCost(route);
        request.setEstimatedCost(estimatedCost);

        ShippingRequest returned = shippingRequestRepository.save(request);

        return ShippingRequestResponseDTO.fromEntity(returned);
    }

    private Address createOrFindAddress(AddressRequestDTO dto) {
        Address existing = addressRepository.findByLatitudeAndLongitude(
                dto.getLatitude(), dto.getLongitude());

        if (existing != null)
            return existing;

        Address address = AddressRequestDTO.toEntity(dto);
        return addressRepository.save(address);
    }

    private boolean isContainerAvailable(Container container) {
        return container.getStatus() == ContainerStatus.LIBRE;
    }

}
