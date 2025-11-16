package com.example.ShippingMicroservice.service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.ShippingMicroservice.dto.address.AddressRequestDTO;
import com.example.ShippingMicroservice.dto.shipping_request.AsignTruckDTO;
import com.example.ShippingMicroservice.dto.shipping_request.CreateShippingRequestDTO;
import com.example.ShippingMicroservice.dto.shipping_request.ShippingRequestResponseDTO;
import com.example.ShippingMicroservice.exception.BadRequestException;
import com.example.ShippingMicroservice.exception.NotFoundException;
import com.example.ShippingMicroservice.model.Address;
import com.example.ShippingMicroservice.model.Container;
import com.example.ShippingMicroservice.model.ContainerStatus;
import com.example.ShippingMicroservice.model.Route;
import com.example.ShippingMicroservice.model.Section;
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
    private final ContainerService containerService;

    @Transactional
    public ShippingRequestResponseDTO createShippingRequest(CreateShippingRequestDTO dto) {
        Container container;
        if (dto.getContainerId() != null) {
            container = containerRepository.findById(dto.getContainerId())
                    .orElseThrow(() -> new BadRequestException(
                            "Container not found with ID: " + dto.getContainerId()));

            if (!isContainerAvailable(container)) {
                throw new BadRequestException("Container is not available for shipping");
            }
        } else {
            container = containerService.create(dto.getContainer());
        }

        Address startAddress = createOrFindAddress(dto.getStartAddress());
        Address endAddress = createOrFindAddress(dto.getEndAddress());

        Route route = routeCreationService.createRouteWithDeposits(startAddress, endAddress);

        BigDecimal estimatedCost = costCalculationService.estimateCost(route, container);
        String estimatedTime = calculateEstimatedTime(route);

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

        containerService.asignate(container.getCode(), ContainerStatus.ASIGNADO);

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
    public ShippingRequestResponseDTO getShippingRequestByContainerCode(String containerCode, Long clientId) {
        ShippingRequest request = shippingRequestRepository.findByContainer_CodeAndClientId(containerCode, clientId)
                .orElseThrow(() -> new NotFoundException("ShippingRequest with Container Code " + containerCode
                        + " for Client " + clientId + " not found"));

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
        for (Section s : request.getRoute().getSections()) {
            if (s.getStatus() != SectionStatus.FINALIZADO) {
                s.setStatus(SectionStatus.CANCELADO);
            }
            
        }
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

        BigDecimal estimatedCost = costCalculationService.estimateCost(route, request.getContainer());
        request.setEstimatedCost(estimatedCost);

        ShippingRequest returned = shippingRequestRepository.save(request);

        return ShippingRequestResponseDTO.fromEntity(returned);
    }

    @Transactional
    public ShippingRequestResponseDTO startSection(Long requestId, Long sectionId) {
        ShippingRequest request = shippingRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("ShippingRequest", requestId));

        Route route = request.getRoute();
        if (route == null) {
            throw new NotFoundException("Route for ShippingRequest", requestId);
        }

        Section section = route.getSections().stream()
                .filter(s -> s.getId().equals(sectionId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Section", sectionId));

        if (section.getStatus() != SectionStatus.ASIGNADO) {
            throw new IllegalStateException(
                    "Section " + sectionId + " cannot be started because it is " + section.getStatus().getValue());
        }

        var sections = route.getSections();
        int index = -1;
        for (int i = 0; i < sections.size(); i++) {
            if (sections.get(i).getId().equals(sectionId)) {
                index = i;
                break;
            }
        }

        if (index > 0) {
            Section previous = sections.get(index - 1);
            if (previous.getStatus() != SectionStatus.FINALIZADO) {
                throw new IllegalStateException(
                        "Section " + sectionId + " cannot be started because previous section "
                                + previous.getId() + " is not FINALIZADO (current: " + previous.getStatus() + ")");
            }
        }

        section.setDatetimeStart(LocalDateTime.now());
        section.setStatus(SectionStatus.INICIADO);
        if (request.getStatus() != ShippingRequestStatus.INICIADO)
            request.setStatus(ShippingRequestStatus.INICIADO);

        ShippingRequest saved = shippingRequestRepository.save(request);
        return ShippingRequestResponseDTO.fromEntity(saved);
    }

    @Transactional
    public ShippingRequestResponseDTO finishSection(Long requestId, Long sectionId) {
        ShippingRequest request = shippingRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("ShippingRequest", requestId));

        Route route = request.getRoute();
        if (route == null) {
            throw new NotFoundException("Route for ShippingRequest", requestId);
        }

        Section section = route.getSections().stream()
                .filter(s -> s.getId().equals(sectionId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Section", sectionId));

        if (section.getStatus() != SectionStatus.INICIADO) {
            throw new IllegalStateException(
                    "Section " + sectionId + " cannot be finalized because its status is " + section.getStatus());
        }

        section.setDatetimeEnd(LocalDateTime.now());
        section.setStatus(SectionStatus.FINALIZADO);
        if (isShippingFinalized(request)) {
            BigDecimal finalCost = costCalculationService.calculateTotalCost(route, request.getContainer());
            request.setFinalCost(finalCost);
            request.setRealTime(calculateTotalTime(route));
            request.setStatus(ShippingRequestStatus.FINALIZADO);
        }

        ShippingRequest saved = shippingRequestRepository.save(request);
        return ShippingRequestResponseDTO.fromEntity(saved);
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

    private boolean isShippingFinalized(ShippingRequest request) {
        for (Section s : request.getRoute().getSections()) {
            if (s.getStatus() != SectionStatus.FINALIZADO)
                return false;
        }
        return true;
    }

    private String calculateTotalTime(Route route) {
        if (route == null || route.getSections() == null || route.getSections().isEmpty()) {
            return "0d, 0h";
        }

        Section first = route.getSections().get(0);
        Section last = route.getSections().get(route.getSections().size() - 1);

        if (first.getDatetimeStart() == null || last.getDatetimeEnd() == null) {
            return "0d, 0h";
        }

        Duration duration = Duration.between(first.getDatetimeStart(), last.getDatetimeEnd());
        if (duration.isNegative()) {
            duration = duration.negated();
        }

        long totalHours = duration.toHours();
        long days = totalHours / 24;
        long hours = totalHours % 24;

        return String.format("%dd, %dh", days, hours);

    }

    private String calculateEstimatedTime(Route route) {
        int hours = 2 + (route.getNumDeposit() * 8) + (int) Math.round(route.getTotalDistance() / 100);
        return hours + " hours";
    }

}
