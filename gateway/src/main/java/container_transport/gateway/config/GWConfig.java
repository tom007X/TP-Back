package container_transport.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GWConfig {

    @Bean
    public RouteLocator configurarRutas(RouteLocatorBuilder builder,
                                        @Value("${routing.microservice.trucks}") String uriTrucks
                                        ){
        return builder.routes()
                .route(p -> p.path("/api/v1/trucks/**").uri(uriTrucks))
                .build();
    }

}
