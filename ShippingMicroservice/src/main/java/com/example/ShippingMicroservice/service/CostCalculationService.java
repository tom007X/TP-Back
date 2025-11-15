package com.example.ShippingMicroservice.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.ShippingMicroservice.model.Route;
import com.example.ShippingMicroservice.model.Section;
import com.example.ShippingMicroservice.model.Tariff;
import com.example.ShippingMicroservice.repository.TariffRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
class CostCalculationService {

    private static final BigDecimal DEFAULT_BASE_COST = new BigDecimal("100.00");
    private static final BigDecimal DEFAULT_COST_PER_KM = new BigDecimal("250.00");
    private static final BigDecimal DEFAULT_COST_PER_DEPOSIT = new BigDecimal("50.00");
    private final TariffRepository tariffRepository;

    public BigDecimal estimateCost(Route route) {
        BigDecimal baseCost      = tariffOrDefault("Base",    "_",        DEFAULT_BASE_COST);
        BigDecimal costPerKm     = tariffOrDefault("Truck",   "distance", DEFAULT_COST_PER_KM);
        BigDecimal costPerDeposit= tariffOrDefault("Deposit", "day",      DEFAULT_COST_PER_DEPOSIT);

        BigDecimal distanceCost = calculateDistanceCost(route, costPerKm);

        BigDecimal depositCost = costPerDeposit.multiply(
                BigDecimal.valueOf(route.getNumDeposit())
        );

        return baseCost.add(distanceCost).add(depositCost);
    }

    public String calculateEstimatedTime(Route route) {
        int hours = 2 + (route.getNumDeposit() * 8) + (int) Math.round(route.getTotalDistance() / 100);
        return hours + " hours";
    }

    private BigDecimal calculateDistanceCost(Route route, BigDecimal costPerKm) {
        if (route.getSections() == null || route.getSections().isEmpty()) {
            return costPerKm.multiply(BigDecimal.valueOf(route.getTotalDistance()));
        }

        BigDecimal distanceCost = BigDecimal.ZERO;
        for (Section s : route.getSections()) {
            if (s.getRealCost() != null) {
                distanceCost = distanceCost.add(s.getRealCost());
            } else {
                distanceCost = distanceCost.add(
                        costPerKm.multiply(BigDecimal.valueOf(s.getDistance()))
                );
            }
        }
        return distanceCost;
    }

    
    private BigDecimal tariffOrDefault(String subject, String metric, BigDecimal def) {
        return findTariffValue(subject, metric).orElse(def);
    }

    private Optional<BigDecimal> findTariffValue(String subject, String metric) {
        Optional<Tariff> t = tariffRepository.findBySubjectAndMetricAndAvailableIsTrue(subject, metric);

        return t.map(Tariff::getValue).filter(Objects::nonNull);
    }

}
