package com.fullstyle.practica1barrosso;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "app.auth.username=test-user",
        "app.auth.password=Test-password-123!",
        "app.jwt.secret=01234567890123456789012345678901"
})
class Practica1barrossoApplicationTests {

    @Test
    void contextLoads() {
    }
}
