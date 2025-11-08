package com.example.ShippingMicroservice.directions.google;

import java.util.List;

import lombok.Data;

@Data
public class GoogleMapsDirectionsResponse {
    private List<GoogleMapsRoute> routes;
    private String status;
}
