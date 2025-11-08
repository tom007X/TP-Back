package com.example.ShippingMicroservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "google.maps")
public class GoogleMapsProperties {
    private String directionsUrl = "https://maps.googleapis.com/maps/api/directions/json";
    private String apiKey;
}
