package com.example.ShippingMicroservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.example.ShippingMicroservice.model.Address;
import com.example.ShippingMicroservice.model.Deposit;
import com.example.ShippingMicroservice.model.Route;
import com.example.ShippingMicroservice.model.Section;
import com.example.ShippingMicroservice.repository.DepositRepositoryImpl;
import com.example.ShippingMicroservice.repository.RouteRepositoryImpl;
import com.example.ShippingMicroservice.repository.SectionRepositoryImpl;
import com.example.ShippingMicroservice.service.interfaces.DirectionsService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RouteCreationService {
    
    private final RouteRepositoryImpl routeRepository;
    private final SectionRepositoryImpl sectionRepository;
    private final DepositRepositoryImpl depositRepository;
    private final DirectionsService directionsService;
     
    /**
     * Creates a route with optimal intermediate deposits using Google Maps API
     */
    @Transactional
    public Route createRouteWithDeposits(Address startAddress, Address endAddress) {
        List<Deposit> availableDeposits = getDepositsInCircle(startAddress, endAddress);
        
        List<Deposit> intermediateDeposits = filterIntermediateDeposits(
            availableDeposits, startAddress, endAddress);
        
        if (intermediateDeposits.isEmpty()) {
            throw new IllegalStateException("No intermediate deposits found between start and end addresses");
        }

        var optimizedRoute = directionsService.optimize(
                startAddress,
                endAddress,
                intermediateDeposits
        );
                 
        // Create the route entity
        Route route = new Route();
        route.setStartAddress(startAddress);
        route.setEndAddress(endAddress);
        route.setNumDeposit(optimizedRoute.getSelectedDeposits().size());
        route.setNumSections(optimizedRoute.getSelectedDeposits().size() + 1);
        
        Route savedRoute = routeRepository.save(route);
        
        // Create sections with actual distances from Google Maps
        createSectionsWithDistances(savedRoute, startAddress, endAddress, 
                                    optimizedRoute.getSelectedDeposits(),
                                    optimizedRoute.getSectionDistances());
        
        return savedRoute;
    }
    
    
    /**
     * Creates sections with actual road distances
     */
    private void createSectionsWithDistances(Route route, Address start, Address end,
                                             List<Deposit> deposits, List<Double> distances) {
        List<Section> sections = new ArrayList<>();
        Address currentStart = start;
        int distanceIndex = 0;
        
        // Create sections through each deposit
        for (Deposit deposit : deposits) {
            Section section = new Section();
            section.setRoute(route);
            section.setStartAddress(currentStart);
            section.setEndAddress(deposit.getAddress());
            section.setDepositAtEnd(deposit);
            
            // Set actual distance if available
            if (distanceIndex < distances.size()) {
                section.setDistance(distances.get(distanceIndex));
            }
            
            sections.add(section);
            currentStart = deposit.getAddress();
            distanceIndex++;
        }
        
        // Final section from last deposit to end
        Section finalSection = new Section();
        finalSection.setRoute(route);
        finalSection.setStartAddress(currentStart);
        finalSection.setEndAddress(end);
        if (distanceIndex < distances.size()) {
            finalSection.setDistance(distances.get(distanceIndex));
        }
        sections.add(finalSection);
        
        sectionRepository.saveAll(sections);
    }
    
    /**
     * Filters out deposits that are at the start or end addresses
     */
    private List<Deposit> filterIntermediateDeposits(List<Deposit> deposits, 
                                                      Address startAddress, 
                                                      Address endAddress) {
        return deposits.stream()
            .filter(deposit -> !isSameLocation(deposit.getAddress(), startAddress) &&
                              !isSameLocation(deposit.getAddress(), endAddress))
            .collect(Collectors.toList());
    }
    
    /**
     * Checks if two addresses are at the same location
     */
    private boolean isSameLocation(Address addr1, Address addr2) {
        if (addr1.getId() != null && addr2.getId() != null && 
            addr1.getId().equals(addr2.getId())) {
            return true;
        }
        
        double TOLERANCE = 0.00001;
        return Math.abs(addr1.getLatitude() - addr2.getLatitude()) < TOLERANCE &&
               Math.abs(addr1.getLongitude() - addr2.getLongitude()) < TOLERANCE;
    }

    
    /**
     * Calculates distance using Haversine formula (for fallback)
     */
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
    
    /**
     * Calculates midpoint for circular search
     */
    private Address calculateMidpoint(Address start, Address end) {
        Address midpoint = new Address();
        midpoint.setLatitude((start.getLatitude() + end.getLatitude()) / 2);
        midpoint.setLongitude((start.getLongitude() + end.getLongitude()) / 2);
        return midpoint;
    }
    
    /**
     * Gets deposits within circular area
     */
    private List<Deposit> getDepositsInCircle(Address start, Address end) {
        Address center = calculateMidpoint(start, end);
        double radius = calculateDistance(start, end) / 2;
        
        return depositRepository.findDepositsWithinRadius(
            center.getLatitude(), 
            center.getLongitude(), 
            radius
        );
    }
}
