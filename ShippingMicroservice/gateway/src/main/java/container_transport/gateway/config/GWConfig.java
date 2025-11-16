package container_transport.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
@EnableWebFluxSecurity
public class GWConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public RouteLocator configurarRutas(RouteLocatorBuilder builder,
            @Value("${routing.microservice.trucks}") String uriTrucks,
            @Value("${routing.microservice.clients}") String uriClients,
            @Value("${routing.microservice.shipping}") String uriShipping) {
                System.out.println(">>> uriTrucks = " + uriTrucks);
                System.out.println(">>> uriClients = " + uriClients);
                System.out.println(">>> uriShipping = " + uriShipping);
        return builder.routes()
                .route(p -> p.path("/api/v1/trucks/**").uri(uriTrucks))
                .route(p -> p.path("/api/v1/drivers/**").uri(uriTrucks))
                .route(p -> p.path("/api/v1/clients/**").uri(uriClients))
                .route(p -> p.path("/api/v1/addresses/**").uri(uriShipping))
                .route(p -> p.path("/api/v1/deposits/**").uri(uriShipping))
                .build();
    }
}
