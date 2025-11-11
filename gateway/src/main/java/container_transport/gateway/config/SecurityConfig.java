package container_transport.gateway.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.Customizer;

import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    // Keep default security chain
    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
            .authorizeExchange(ex -> ex
                .pathMatchers("/", "/public/**").permitAll()
                .anyExchange().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
            .build();
    }

    // Custom ReactiveJwtDecoder that validates signatures using Keycloak internal jwks
    // but accepts tokens whose "iss" claim is either the internal Docker hostname
    // or the host-mapped localhost (useful for testing with Postman).
    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() {
        String internalIssuer = "http://keycloak:8080/realms/tpi-backend";
        String hostIssuer = "http://localhost:8081/realms/tpi-backend";

        // create decoder from the internal issuer (so the container can fetch JWKS)
        ReactiveJwtDecoder decoder = ReactiveJwtDecoders.fromOidcIssuerLocation(internalIssuer);

        // Build a validator that accepts either issuer
        OAuth2TokenValidator<Jwt> withTimestamp = new JwtTimestampValidator();
        OAuth2TokenValidator<Jwt> issuerValidator = token -> {
            if (token == null || token.getIssuer() == null) {
                return OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_token", "Missing issuer claim", null));
            }
            String iss = token.getIssuer().toString();
            List<String> allowed = Arrays.asList(internalIssuer, hostIssuer);
            if (allowed.contains(iss)) {
                return OAuth2TokenValidatorResult.success();
            }
            return OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_token", "The required issuer is missing", null));
        };

        DelegatingOAuth2TokenValidator<Jwt> delegating = new DelegatingOAuth2TokenValidator<>(withTimestamp, issuerValidator);

        if (decoder instanceof NimbusReactiveJwtDecoder) {
            ((NimbusReactiveJwtDecoder) decoder).setJwtValidator(delegating);
        }

        return decoder;
    }
}
