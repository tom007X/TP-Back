package com.example.ShippingMicroservice.dto.shipping_request;

import java.util.List;

import com.example.ShippingMicroservice.model.Deposit;

import lombok.Data;

@Data
public class OptimizedRouteResult {

    private List<Deposit> selectedDeposits;
    private List<Double> sectionDistances;
    
    public OptimizedRouteResult(List<Deposit> selectedDeposits, List<Double> sectionDistances) {
        this.selectedDeposits = selectedDeposits;
        this.sectionDistances = sectionDistances;
    }
    
    public List<Deposit> getSelectedDeposits() { return selectedDeposits; }
    public List<Double> getSectionDistances() { return sectionDistances; }

}
