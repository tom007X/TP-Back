package container_transport.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
// no direct Jwt decoder bean: let Spring Boot autoconfigure ReactiveJwtDecoder from properties
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.config.web.server.ServerHttpSecurity;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
            .csrf(csrf -> csrf.disable()) // Deshabilitar CSRF para simplificar pruebas
            .authorizeExchange(ex -> ex
                .pathMatchers("/", "/public/**").permitAll()
                .anyExchange().authenticated()
            )
            // Dejar que Spring Boot autoconfigure el ReactiveJwtDecoder usando la propiedad
            // spring.security.oauth2.resourceserver.jwt.issuer-uri (establecida en application.yml / env)
            .oauth2ResourceServer(oauth2 -> oauth2.jwt())
            .build();
    }

}
