package com.fullstyle.practica1barrosso;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "app.auth.username=test-user",
        "app.auth.password=Test-password-123!",
        "app.jwt.secret=01234567890123456789012345678901",
        "server.ssl.enabled=false",
        "server.ssl.key-store-password=test-password"
})
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
class SecurityConfigTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void doesNotRequireAuthenticationForHealthPath() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isNotFound());
    }

    @Test
    void allowsAuthRoutesWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/api/auth/test"))
                .andExpect(status().isNotFound());
    }

    @Test
    void rejectsProtectedEndpointWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/api/private"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void deniesAdminEndpointToUserWithoutAdminRole() throws Exception {
        mockMvc.perform(get("/api/admin/private"))
                .andExpect(status().isForbidden());
    }
}
