package com.boilerworks.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestConfig.class)
class BoilerworksApplicationTest {

    @Test
    void contextLoads() {
        // Verifies the Spring context starts successfully
    }
}
