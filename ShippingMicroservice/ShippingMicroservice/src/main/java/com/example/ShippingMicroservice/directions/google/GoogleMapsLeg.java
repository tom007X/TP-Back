package com.example.ShippingMicroservice.directions.google;

import lombok.Data;

@Data
public class GoogleMapsLeg {
    private GoogleMapsDistance distance;
    private GoogleMapsDuration duration;
}
