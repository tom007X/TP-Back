package container_transport.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/public/ping")
    public String ping() {
        return "pong";
    }

    @GetMapping("/hello-1")
    public String helloAdmin() {
        return "Prueba prueba - ADMIN";
    }

    @GetMapping("/hello-2")
    public String helloClient() {
        return "Prueba prueba - CLIENT";
    }
}
