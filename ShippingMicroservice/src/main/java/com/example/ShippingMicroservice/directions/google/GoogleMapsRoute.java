package com.example.ShippingMicroservice.directions.google;

import java.util.List;

import lombok.Data;

@Data
public class GoogleMapsRoute {
    private List<GoogleMapsLeg> legs;
    private List<Integer> waypointOrder;
}
