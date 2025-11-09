package com.example.ShippingMicroservice.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.example.ShippingMicroservice.model.Container;
import com.example.ShippingMicroservice.model.Route;

@Service
class CostCalculationService {

    private static final BigDecimal BASE_COST = new BigDecimal("100.00");
    private static final BigDecimal COST_PER_KM = new BigDecimal("2.50");
    private static final BigDecimal COST_PER_DEPOSIT = new BigDecimal("50.00");

    public BigDecimal calculateCost(Route route, Container container) {
        // Calculate total distance from route sections
        // This is a simplified calculation - adjust based on your business logic
        BigDecimal distanceCost = COST_PER_KM.multiply(new BigDecimal("100")); // placeholder

        BigDecimal depositCost = COST_PER_DEPOSIT.multiply(
                new BigDecimal(route.getNumDeposit()));

        return BASE_COST.add(distanceCost).add(depositCost);
    }

    public String calculateEstimatedTime(Route route) {
        // Calculate estimated time based on route
        // This is a simplified calculation
        int hours = 2 + (route.getNumDeposit() * 1);
        return hours + " hours";
    }
}
