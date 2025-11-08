package com.example.ShippingMicroservice.service.interfaces;

import java.util.List;

import com.example.ShippingMicroservice.dto.OptimizedRouteResult;
import com.example.ShippingMicroservice.model.Address;
import com.example.ShippingMicroservice.model.Deposit;

public interface DirectionsService {
    OptimizedRouteResult optimize(Address start, Address end, List<Deposit> waypoints);
}
