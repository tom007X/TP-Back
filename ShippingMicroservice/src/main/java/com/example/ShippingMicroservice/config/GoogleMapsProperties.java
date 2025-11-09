package com.example.ShippingMicroservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "google.maps")
public record GoogleMapsProperties(String directionsUrl, String apiKey) {}
