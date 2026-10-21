package com.fullstyle.practica1barrosso;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "app.auth.username=test-user",
        "app.auth.password=Test-password-123!",
        "app.jwt.secret=01234567890123456789012345678901",
        "app.cors.allowed-origin=http://localhost:4200",
        "server.ssl.enabled=false",
        "server.ssl.key-store-password=test-password"
})
@AutoConfigureMockMvc
class CorsConfigTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void allowsAngularDevelopmentOrigin() throws Exception {
        mockMvc.perform(options("/api/private")
                        .header(HttpHeaders.ORIGIN, "http://localhost:4200")
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET")
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, "authorization"))
                .andExpect(status().isOk())
                .andExpect(header().string(
                        HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:4200"));
    }

    @Test
    void rejectsUnexpectedOrigin() throws Exception {
        mockMvc.perform(options("/api/private")
                        .header(HttpHeaders.ORIGIN, "https://example.invalid")
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET"))
                .andExpect(status().isForbidden())
                .andExpect(header().doesNotExist(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN));
    }
}
