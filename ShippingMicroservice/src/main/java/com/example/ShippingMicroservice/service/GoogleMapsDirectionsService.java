package com.example.ShippingMicroservice.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.ShippingMicroservice.config.GoogleMapsProperties;
import com.example.ShippingMicroservice.directions.google.GoogleMapsDirectionsResponse;
import com.example.ShippingMicroservice.dto.shipping_request.OptimizedRouteResult;
import com.example.ShippingMicroservice.exception.DirectionsException;
import com.example.ShippingMicroservice.model.Address;
import com.example.ShippingMicroservice.model.Deposit;
import com.example.ShippingMicroservice.service.interfaces.DirectionsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GoogleMapsDirectionsService implements DirectionsService {

    private final RestTemplate restTemplate;
    private final GoogleMapsProperties properties;

    private static final int MAX_OPTIMIZABLE_WAYPOINTS = 5;

    @Override
    public OptimizedRouteResult optimize(Address start, Address end, List<Deposit> waypoints) {
        List<Deposit> limited = waypoints == null ? List.of()
                : waypoints.subList(0, Math.min(waypoints.size(), MAX_OPTIMIZABLE_WAYPOINTS));
        String directionsUrl = properties.directionsUrl();
        String apiKey = properties.apiKey();

        URI uri = UriComponentsBuilder
                .fromUriString(directionsUrl)
                .queryParam("origin", toCoordinates(start))
                .queryParam("destination", toCoordinates(end))
                .queryParam("waypoints", buildWaypointsParam(limited))
                .queryParam("key", apiKey)
                .build()
                .toUri();

        try {
            ResponseEntity<GoogleMapsDirectionsResponse> resp = restTemplate.getForEntity(uri,
                    GoogleMapsDirectionsResponse.class);

            GoogleMapsDirectionsResponse body = resp.getBody();
            if (body == null || !"OK".equalsIgnoreCase(body.getStatus())) {
                throw new DirectionsException("Google Directions failed: " +
                        (body == null ? "null body" : body.getStatus()));
            }

            return parse(body, limited, start, end);

        } catch (Exception e) {
            throw new DirectionsException("Error calling Google Directions API", e);
        }
    }

    private String toCoordinates(Address a) {
        return a.getLatitude() + "," + a.getLongitude();
    }

    private String buildWaypointsParam(List<Deposit> waypoints) {
        if (waypoints.isEmpty())
            return "optimize:true";
        String joined = waypoints.stream()
                .map(d -> d.getAddress())
                .map(this::toCoordinates)
                .collect(Collectors.joining("|"));
        return "optimize:true|" + joined;
    }

    private OptimizedRouteResult parse(GoogleMapsDirectionsResponse response,
            List<Deposit> candidateWaypoints,
            Address start,
            Address end) {
        // Take the first route
        var routes = response.getRoutes();
        if (routes == null || routes.isEmpty()) {
            throw new DirectionsException("No routes returned by Google Directions");
        }
        var route = routes.get(0);

        // Reorder waypoints
        List<Integer> waypointOrder = route.getWaypointOrder();
        List<Deposit> optimizedDeposits = new ArrayList<>();
        if (waypointOrder != null && !waypointOrder.isEmpty()) {
            for (Integer idx : waypointOrder) {
                optimizedDeposits.add(candidateWaypoints.get(idx));
            }
        } else {
            optimizedDeposits = new ArrayList<>(candidateWaypoints);
        }

        // Extract legs -> distances km
        List<Double> legDistancesKm = new ArrayList<>();
        if (route.getLegs() != null) {
            route.getLegs().forEach(leg -> {
                if (leg.getDistance() != null) {
                    legDistancesKm.add(leg.getDistance().getValue() / 1000.0);
                }
            });
        }

        // Ensure we have exactly (waypoints + 1) legs (start->w1, w1->w2, ...,
        // last->end)
        // If not, we simply trust the API’s legs; consumers should handle size
        // mismatches gracefully.
        return new OptimizedRouteResult(optimizedDeposits, legDistancesKm);
    }

}
