package container_transport.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
// @EnableWebFluxSecurity
public class GWConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public RouteLocator configurarRutas(RouteLocatorBuilder builder,
    @Value("http://localhost:8090") String uriShipping,
    @Value("http://localhost:8091") String uriTruck,
    @Value("http://localhost:8092") String uriClient) {
        return builder.routes()

        // TRUCKS MICROSERVICE
            .route("trucks-route", p -> p
                .path("/api/v1/trucks/**")
                .uri(uriTruck)
                )
            .route("drivers-route", p -> p
                .path("/api/v1/drivers/**")
                .uri(uriTruck)
                )
        
        // CLIENTS MICROSERVICE
            .route("clients-route", p -> p
                .path("/api/v1/clients/**")
                .uri(uriClient)
                )
        
        // SHIPPING MICROSERVICE
            .route("shipping-addresses-route", p -> p
                .path("/api/v1/addresses")
                .uri(uriShipping)
                )
            .route("shipping-deposits-route", p -> p
                .path("/api/v1/deposits")
                .uri(uriShipping)
                )                
            .build();
    }
}
