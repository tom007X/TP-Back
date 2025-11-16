package com.example.ShippingMicroservice.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.ShippingMicroservice.model.Container;
import com.example.ShippingMicroservice.model.Route;
import com.example.ShippingMicroservice.model.Section;
import com.example.ShippingMicroservice.model.SectionType;
import com.example.ShippingMicroservice.model.Tariff;
import com.example.ShippingMicroservice.repository.TariffRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
class CostCalculationService {

    private static final BigDecimal DEFAULT_BASE_COST = new BigDecimal("100.00");
    private static final BigDecimal DEFAULT_COST_PER_KM = new BigDecimal("250.00");
    private static final BigDecimal DEFAULT_COST_PER_DEPOSIT_DAY = new BigDecimal("50.00");
    private static final BigDecimal DEFAULT_COST_PER_VOLUME = new BigDecimal("5.00");
    private static final BigDecimal DEFAULT_COST_PER_WEIGHT = new BigDecimal("10.00");
    private final TariffRepository tariffRepository;

    public BigDecimal estimateCost(Route route, Container container) {
        BigDecimal baseCost = tariffOrDefault("Base", "_", DEFAULT_BASE_COST);
        BigDecimal costPerKm = tariffOrDefault("Truck", "distance", DEFAULT_COST_PER_KM);
        BigDecimal costPerDeposit = tariffOrDefault("Deposit", "day", DEFAULT_COST_PER_DEPOSIT_DAY);

        BigDecimal distanceCost = calculateDistanceCost(route, costPerKm);
        BigDecimal containerCost = calculateCostPerContainer(container);

        BigDecimal depositCost = costPerDeposit.multiply(
                BigDecimal.valueOf(route.getNumDeposit()));

        return baseCost.add(distanceCost).add(depositCost).add(containerCost);
    }

    public BigDecimal calculateTotalCost(Route route, Container container) {
        BigDecimal baseCost = tariffOrDefault("Base", "_", DEFAULT_BASE_COST);
        BigDecimal costPerKm = tariffOrDefault("Truck", "distance", DEFAULT_COST_PER_KM);
        BigDecimal costPerDeposit = tariffOrDefault("Deposit", "day", DEFAULT_COST_PER_DEPOSIT_DAY);

        BigDecimal distanceCost = calculateDistanceCost(route, costPerKm);
        BigDecimal depositCost = calculateDepositCost(route, costPerDeposit);
        BigDecimal containerCost = calculateCostPerContainer(container);

        return baseCost.add(distanceCost).add(depositCost).add(containerCost);
    }

    private BigDecimal calculateDepositCost(Route route, BigDecimal costPerDepositDay) {

        if (route == null)
            return BigDecimal.ZERO;

        List<Section> sections = route.getSections();
        if (sections == null || sections.size() < 2)
            return BigDecimal.ZERO;

        BigDecimal totalDepositCost = BigDecimal.ZERO;

        for (int i = 1; i < sections.size(); i++) {
            Section current = sections.get(i);
            Section previous = sections.get(i - 1);

            if (current.getType() != SectionType.DEPOSITO_DEPOSITO
                    && current.getType() != SectionType.DEPOSITO_DESTINO)
                continue;

            LocalDateTime prevEnd = previous.getDatetimeEnd();
            LocalDateTime currStart = current.getDatetimeStart();

            if (prevEnd == null || currStart == null)
                continue;

            long daysBetween = ChronoUnit.DAYS.between(prevEnd, currStart);

            if (daysBetween > 0) {
                BigDecimal periodCost = costPerDepositDay.multiply(BigDecimal.valueOf(daysBetween));
                totalDepositCost = totalDepositCost.add(periodCost);
            }
        }

        return totalDepositCost;
    }

    private BigDecimal calculateCostPerContainer(Container container) {
        BigDecimal costPerVolume = tariffOrDefault("Container", "volume", DEFAULT_COST_PER_VOLUME);
        BigDecimal costPerWeight = tariffOrDefault("Container", "weight", DEFAULT_COST_PER_WEIGHT);

        BigDecimal volumeCost = costPerVolume.multiply(
                container.getVolume());
        BigDecimal weightCost = costPerWeight.multiply(
                container.getWeight());

        return volumeCost.add(weightCost);
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
                        costPerKm.multiply(BigDecimal.valueOf(s.getDistance())));
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
