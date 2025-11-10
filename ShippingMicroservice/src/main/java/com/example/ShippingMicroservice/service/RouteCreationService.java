package com.example.ShippingMicroservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.example.ShippingMicroservice.model.Address;
import com.example.ShippingMicroservice.model.Deposit;
import com.example.ShippingMicroservice.model.Route;
import com.example.ShippingMicroservice.model.Section;
import com.example.ShippingMicroservice.model.SectionStatus;
import com.example.ShippingMicroservice.model.SectionType;
import com.example.ShippingMicroservice.repository.DepositRepository;
import com.example.ShippingMicroservice.service.interfaces.DirectionsService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RouteCreationService {

    private final DepositRepository depositRepository;
    private final DirectionsService directionsService;

    @Transactional
    public Route createRouteWithDeposits(Address startAddress, Address endAddress) {
        List<Deposit> availableDeposits = getDepositsInCircle(startAddress, endAddress);

        List<Deposit> intermediateDeposits = filterIntermediateDeposits(
                availableDeposits, startAddress, endAddress);

        var optimizedRoute = directionsService.optimize(
                startAddress,
                endAddress,
                intermediateDeposits);

        double totalDistance = optimizedRoute.getSectionDistances()
                .stream()
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .sum();

        Route route = new Route();
        route.setStartAddress(startAddress);
        route.setEndAddress(endAddress);
        route.setNumDeposit(optimizedRoute.getSelectedDeposits().size());
        route.setNumSections(optimizedRoute.getSelectedDeposits().size() + 1);
        route.setTotalDistance(totalDistance);

        createSectionsWithDistances(route, startAddress, endAddress,
                optimizedRoute.getSelectedDeposits(),
                optimizedRoute.getSectionDistances());

        return route;
    }

    private void createSectionsWithDistances(Route route, Address start, Address end,
            List<Deposit> deposits, List<Double> distances) {

        List<Section> sections = new ArrayList<>();
        int distanceIndex = 0;

        if (deposits.isEmpty()) {
            Section directSection = new Section();
            directSection.setRoute(route);
            directSection.setStartAddress(start);
            directSection.setEndAddress(end);
            directSection.setStatus(SectionStatus.ESTIMADO);
            directSection.setType(SectionType.ORIGEN_DESTINO);
            if (distanceIndex < distances.size())
                directSection.setDistance(distances.get(distanceIndex));
            sections.add(directSection);
            sections.stream().forEach(s -> route.addSection(s));
            return;
        }
        Deposit currentDeposit = deposits.get(0);
        Address currentStart = start;

        for (int i = 0; i < deposits.size(); i++) {
            Deposit deposit = deposits.get(i);
            Section section = new Section();

            section.setRoute(route);
            section.setStartAddress(currentStart);
            section.setEndAddress(deposit.getAddress());
            section.setDepositAtEnd(deposit);
            section.setStatus(SectionStatus.ESTIMADO);
            if (distanceIndex < distances.size())
                section.setDistance(distances.get(distanceIndex));
            if (i == 0)
                section.setType(SectionType.ORIGEN_DEPOSITO);
            else {
                section.setType(SectionType.DEPOSITO_DEPOSITO);
                section.setDepositAtStart(currentDeposit);
            }
            sections.add(section);
            currentStart = deposit.getAddress();
            currentDeposit = deposit;
            distanceIndex++;
        }

        Section finalSection = new Section();
        finalSection.setRoute(route);
        finalSection.setStartAddress(currentStart);
        finalSection.setEndAddress(end);
        finalSection.setStatus(SectionStatus.ESTIMADO);
        finalSection.setType(SectionType.DEPOSITO_DESTINO);
        if (distanceIndex < distances.size()) {
            finalSection.setDistance(distances.get(distanceIndex));
        }
        sections.add(finalSection);
        sections.stream().forEach(s -> route.addSection(s));
    }

    private List<Deposit> filterIntermediateDeposits(List<Deposit> deposits,
            Address startAddress,
            Address endAddress) {
        return deposits.stream()
                .filter(deposit -> !isSameLocation(deposit.getAddress(), startAddress) &&
                        !isSameLocation(deposit.getAddress(), endAddress))
                .collect(Collectors.toList());
    }

    private boolean isSameLocation(Address addr1, Address addr2) {
        if (addr1.getId() != null && addr2.getId() != null &&
                addr1.getId().equals(addr2.getId())) {
            return true;
        }

        double TOLERANCE = 0.0001;
        return Math.abs(addr1.getLatitude() - addr2.getLatitude()) < TOLERANCE &&
                Math.abs(addr1.getLongitude() - addr2.getLongitude()) < TOLERANCE;
    }

    private double calculateDistance(Address a1, Address a2) {
        double lat1 = Math.toRadians(a1.getLatitude());
        double lat2 = Math.toRadians(a2.getLatitude());
        double lon1 = Math.toRadians(a1.getLongitude());
        double lon2 = Math.toRadians(a2.getLongitude());

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return 6371 * c;
    }

    private Address calculateMidpoint(Address start, Address end) {
        Address midpoint = new Address();
        midpoint.setLatitude((start.getLatitude() + end.getLatitude()) / 2);
        midpoint.setLongitude((start.getLongitude() + end.getLongitude()) / 2);
        return midpoint;
    }

    private List<Deposit> getDepositsInCircle(Address start, Address end) {
        Address center = calculateMidpoint(start, end);
        double radius = calculateDistance(start, end) / 2;

        return depositRepository.findDepositsWithinRadius(
                center.getLatitude(),
                center.getLongitude(),
                radius);
    }
}
