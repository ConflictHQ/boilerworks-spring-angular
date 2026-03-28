package com.boilerworks.api;

import java.util.Optional;
import java.util.UUID;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.AuditorAware;

@TestConfiguration
public class TestConfig {

  @Bean
  @Primary
  public AuditorAware<UUID> auditorAware() {
    return () -> Optional.empty();
  }
}
