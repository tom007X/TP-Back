package container_transport.gateway.config;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.config.Customizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.bind.annotation.RequestMethod;

import reactor.core.publisher.Mono;

import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;    
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(
            ServerHttpSecurity http,
            ReactiveJwtAuthenticationConverterAdapter jwtAuthenticationConverter) {

        http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange(exchanges -> exchanges
                .pathMatchers("/public/**").permitAll()

                // Shipping requests
                .pathMatchers(HttpMethod.POST, "/api/v1/shipping-requests").hasRole("CLIENT")
                .pathMatchers(HttpMethod.GET, "/api/v1/shipping-requests/**").hasRole("CLIENT")

                // Addresses
                .pathMatchers(HttpMethod.POST, "/api/v1/addresses").hasRole("ADMIN")
                .pathMatchers(HttpMethod.PUT, "/api/v1/addresses/**").hasRole("ADMIN")

                // Deposits
                .pathMatchers(HttpMethod.POST, "/api/v1/deposits").hasRole("ADMIN")
                .pathMatchers(HttpMethod.PUT, "/api/v1/deposits/**").hasRole("ADMIN")

                // Trucks
                .pathMatchers(HttpMethod.GET, "/api/v1/trucks/**").hasRole("ADMIN")
                .pathMatchers(HttpMethod.POST, "/api/v1/trucks").hasRole("ADMIN")
                .pathMatchers(HttpMethod.PUT, "/api/v1/trucks/**").hasRole("ADMIN")

                // Assign truck to section
                .pathMatchers(HttpMethod.PUT, "/api/v1/shipping-requests/{requestId}/sections/{sectionId}/asign-truck")
                    .hasRole("ADMIN")

                // Register start/end of section
                .pathMatchers(HttpMethod.PUT, "/api/v1/trucks/{id}/availability").hasRole("TRANSPORTISTA")

                .anyExchange().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter))
            );

        return http.build();
    }

    @Bean
    public ReactiveJwtAuthenticationConverterAdapter jwtAuthenticationConverter() {
        return new ReactiveJwtAuthenticationConverterAdapter(new KeycloakJwtConverter());
    }

    // Inner class to convert Keycloak JWT to authorities
    private static class KeycloakJwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {
        @Override
        public AbstractAuthenticationToken convert(Jwt jwt) {
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
            List<String> roles = Collections.emptyList();
            
            if (realmAccess != null && realmAccess.get("roles") instanceof List) {
                roles = (List<String>) realmAccess.get("roles");
            }

            List<GrantedAuthority> authorities = roles.stream()
                .map(role -> "ROLE_" + role.toUpperCase())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

            return new JwtAuthenticationToken(jwt, authorities);
        }
    }

    // Adapter to make the converter reactive
    private static class ReactiveJwtAuthenticationConverterAdapter 
            implements Converter<Jwt, Mono<AbstractAuthenticationToken>> {
        
        private final Converter<Jwt, AbstractAuthenticationToken> delegate;

        public ReactiveJwtAuthenticationConverterAdapter(Converter<Jwt, AbstractAuthenticationToken> delegate) {
            this.delegate = delegate;
        }

        @Override
        public Mono<AbstractAuthenticationToken> convert(Jwt jwt) {
            return Mono.just(delegate.convert(jwt));
        }
    }
}
// keycloak mete los roles en el realm_access.roles. Spring security mira por defecto scope o scp. Por ende si no se hace el metodo, los hasRole(...) no van a matchear

// esto hay que usarlo en oauth2ResourceServer