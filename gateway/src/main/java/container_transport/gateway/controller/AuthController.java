package container_transport.gateway.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClient;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
public class AuthController {
    @GetMapping("/api/login/oauth2/code/keycloak")
    public String intercambiarCode(@RequestParam String code) throws UnsupportedEncodingException {
        RestClient restClient = RestClient.create();

        String formData = "grant_type=authorization_code" + "&code=" + URLEncoder.encode(code, StandardCharsets.UTF_8) +
            "&client_id=tpi-backend-client" + "&redirect_uri=" + URLEncoder.encode("http://localhost:8080/api/login/oauth2/code/keycloak", StandardCharsets.UTF_8);

            String token = restClient.post()
                .uri("http://localhost:8081/realms/tpi-backend/protocol/openid-connect/token", StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formData)
                .retrieve()
                .body(String.class);
            log.info("Token recibido desde keycloak: {}", token);
            return "Token recibido y logueado en consola";
    }
}
