package container_transport.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GWConfig {

    @Value("${routing.microservice.trucks:}")
    private String trucks;
    @Value("${routing.microservice.clients:}")
    private String clients;
    @Value("${routing.microservice.shipping:}")
    private String shipping;


@Bean
public RouteLocator configurarRutas(RouteLocatorBuilder builder) {
    var r = builder.routes();

    if (!trucks.isBlank()) {
        r.route("trucks", p -> p.path("/svc/trucks/**")
            .filters(f -> f.stripPrefix(2))
            .uri(trucks));
    }
    if (!clients.isBlank()) {
        r.route("clients", p -> p.path("/svc/clients/**")
            .filters(f -> f.stripPrefix(2))
            .uri(clients));
    }
    if (!shipping.isBlank()) {
        r.route("shipping", p -> p.path("/svc/shipping/**")
            .filters(f -> f.stripPrefix(2))
            .uri(shipping));
    }
    return r.build();
}
}
