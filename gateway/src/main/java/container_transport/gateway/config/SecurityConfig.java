package container_transport.gateway.config;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.config.Customizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;    
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/public/**").permitAll()
                .requestMatchers(RequestMethod.POST.toString(), "/api/v1/shipping-requests").hasRole("CLIENT")
                .requestMatchers(RequestMethod.GET.toString(), "/api/v1/shipping-requests/**").hasRole("CLIENT")
                // Ver el costo y tiempo de entrega estimado

                // Addresses = ciudades?
                .requestMatchers(RequestMethod.POST.toString(), "/api/v1/addresses").hasRole("ADMIN")
                .requestMatchers(RequestMethod.PUT.toString(), "/api/v1/addresses").hasRole("ADMIN")
                // .requestMatchers(RequestMethod.DELETE.toString(), "/api/v1/addresses").hasRole("ADMIN")
                
                // Depositos
                .requestMatchers(RequestMethod.POST.toString(), "/api/v1/deposits").hasRole("ADMIN")
                .requestMatchers(RequestMethod.PUT.toString(), "/api/v1/deposits/**").hasRole("ADMIN")
                // .requestMatchers(RequestMethod.DELETE.toString(), "/api/v1/deposits/**").hasRole("ADMIN") // -> No lo dice el enunciado pero creo que tiene sentido

                // Tarifas (Falta)

                // Camiones
                .requestMatchers(RequestMethod.GET.toString(), "/api/v1/trucks/**").hasRole("ADMIN")
                .requestMatchers(RequestMethod.POST.toString(), "/api/v1/trucks").hasRole("ADMIN")
                .requestMatchers(RequestMethod.PUT.toString(), "/api/v1/trucks/**").hasRole("ADMIN")

                // Contenedores (Falta)

                // Asigna camiones a tramos de traslado
                .requestMatchers(RequestMethod.PUT.toString(), "/{requestId}/sections/{sectionId}/asign-truck").hasRole("ADMIN")

                // Modifica parametros de tarifacion (Falta)

                // Puede ver los tramos asignados que tiene (Falta)

                // Puede registrar un inicio o fin de tramo
                .requestMatchers(RequestMethod.PUT.toString(), "/api/v1/trucks/{id}/availability").hasRole("TRANSPORTISTA")

                // 
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> 
                oauth2.jwt(Customizer.withDefaults()))
            .build();
    }
    
    @Bean
    Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter() {
        return new Converter<Jwt, AbstractAuthenticationToken>() {
            @Override
            public AbstractAuthenticationToken convert(org.springframework.security.oauth2.jwt.Jwt jwt) {
                // Solicito el claim con el que keycloak manda el mapeo de roles
                Map<String, List<String>> realmAccess = jwt.getClaim("realm_access");
                // Se toma cada rol de la lista y se lo convierte a un objeto GranthedAuthority
                List<GrantedAuthority> authorities = realmAccess.get("roles")
                    .stream().map( r-> 
                        String.format("ROLE_%s", r.toUpperCase()))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
                return new JwtAuthenticationToken(jwt, authorities);
            }
        };
    }

}
// keycloak mete los roles en el realm_access.roles. Spring security mira por defecto scope o scp. Por ende si no se hace el metodo, los hasRole(...) no van a matchear

// esto hay que usarlo en oauth2ResourceServer