package com.boilerworks.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.boilerworks.api.TestConfig;
import com.boilerworks.api.model.Product;
import com.boilerworks.api.repository.CategoryRepository;
import com.boilerworks.api.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestConfig.class)
@Transactional
class ProductControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private ProductRepository productRepository;

  @Autowired private CategoryRepository categoryRepository;

  @BeforeEach
  void setUp() {
    productRepository.deleteAll();
    categoryRepository.deleteAll();
  }

  @Test
  @WithMockUser(authorities = {"product.add", "product.view"})
  void createProduct() throws Exception {
    mockMvc
        .perform(
            post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    objectMapper.writeValueAsString(
                        Map.of("name", "Widget", "price", "9.99", "sku", "WDG-001"))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(true))
        .andExpect(jsonPath("$.data.name").value("Widget"))
        .andExpect(jsonPath("$.data.slug").value("widget"));

    Product product = productRepository.findByName("Widget").orElseThrow();
    assertThat(product.getPrice()).isEqualByComparingTo("9.99");
    assertThat(product.getSku()).isEqualTo("WDG-001");
  }

  @Test
  void createProductDeniedWithoutAuth() throws Exception {
    mockMvc
        .perform(
            post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    objectMapper.writeValueAsString(
                        Map.of("name", "Widget", "price", "9.99", "sku", "WDG-001"))))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(authorities = {"product.view"})
  void createProductDeniedWithoutPermission() throws Exception {
    mockMvc
        .perform(
            post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    objectMapper.writeValueAsString(
                        Map.of("name", "Widget", "price", "9.99", "sku", "WDG-001"))))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(authorities = {"product.view"})
  void listProducts() throws Exception {
    Product p = new Product();
    p.setName("Test Product");
    p.setSlug("test-product");
    p.setPrice(BigDecimal.valueOf(19.99));
    p.setSku("TST-001");
    productRepository.save(p);

    mockMvc
        .perform(get("/api/products"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name").value("Test Product"));
  }

  @Test
  @WithMockUser(authorities = {"product.view", "product.change"})
  void updateProduct() throws Exception {
    Product p = new Product();
    p.setName("Old Name");
    p.setSlug("old-name");
    p.setPrice(BigDecimal.valueOf(10.00));
    p.setSku("OLD-001");
    p = productRepository.save(p);

    mockMvc
        .perform(
            put("/api/products/" + p.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("name", "New Name"))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(true))
        .andExpect(jsonPath("$.data.name").value("New Name"));

    Product updated = productRepository.findById(p.getId()).orElseThrow();
    assertThat(updated.getName()).isEqualTo("New Name");
    assertThat(updated.getSlug()).isEqualTo("new-name");
  }

  @Test
  @WithMockUser(authorities = {"product.view", "product.delete"})
  void softDeleteProduct() throws Exception {
    Product p = new Product();
    p.setName("To Delete");
    p.setSlug("to-delete");
    p.setPrice(BigDecimal.valueOf(5.00));
    p.setSku("DEL-001");
    p = productRepository.save(p);

    mockMvc
        .perform(delete("/api/products/" + p.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(true));

    // Soft-deleted: findAll should not return it (due to @SQLRestriction)
    assertThat(productRepository.findAll()).isEmpty();
  }
}
