package com.fullstyle.practica1barrosso;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProtectedController {

    @GetMapping("/api/private")
    public Map<String, String> privateEndpoint() {
        return Map.of("message", "Acceso autorizado");
    }

    @GetMapping("/api/admin/private")
    public Map<String, String> adminEndpoint() {
        return Map.of("message", "Acceso de administrador");
    }
}
