package com.example.ShippingMicroservice.service.interfaces;

import java.util.List;

import com.example.ShippingMicroservice.dto.OptimizedRouteResult;
import com.example.ShippingMicroservice.model.Address;

public interface DirectionsService {
    OptimizedRouteResult optimize(Address start, Address end, List<Address> waypoints);
}
