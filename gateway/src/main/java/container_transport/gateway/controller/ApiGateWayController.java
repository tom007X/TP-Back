package container_transport.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiGateWayController {
    
    @GetMapping("/hello-1")
    public String helloAdmin() {
        return "Hello Desde ADMIN";
    }

    @GetMapping("/hello-2")
    public String helloClient() {
        return "Hello desde CLIENT";
    }

    @GetMapping("/public/ping")
    public String ping() {
        return "pong";
    }
}
