package com.example.ShippingMicroservice.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.example.ShippingMicroservice.model.Route;
import com.example.ShippingMicroservice.model.Section;

@Service
class CostCalculationService {

    private static final BigDecimal BASE_COST = new BigDecimal("100.00");
    private static final BigDecimal COST_PER_KM = new BigDecimal("250.00");
    private static final BigDecimal COST_PER_DEPOSIT = new BigDecimal("50.00");

    public BigDecimal estimateCost(Route route) {
        BigDecimal distanceCost = calculateDistanceCost(route);

        BigDecimal depositCost = COST_PER_DEPOSIT.multiply(
                new BigDecimal(route.getNumDeposit()));

        return BASE_COST.add(distanceCost).add(depositCost);
    }

    public String calculateEstimatedTime(Route route) {
        int hours = 2 + (route.getNumDeposit() * 8) + (int) Math.round(route.getTotalDistance() / 100);
        return hours + " hours";
    }

    private BigDecimal calculateDistanceCost(Route route) {
        if (route.getSections() == null || route.getSections().isEmpty())
            return COST_PER_KM.multiply(new BigDecimal(route.getTotalDistance()));

        BigDecimal distanceCost = BigDecimal.ZERO;
        for (Section s : route.getSections()) {
            if (s.getRealCost() != null)
                distanceCost = distanceCost.add(s.getRealCost());
            else
                distanceCost = distanceCost.add(COST_PER_KM.multiply(new BigDecimal(s.getDistance())));
        }
        return distanceCost;
    }
}
