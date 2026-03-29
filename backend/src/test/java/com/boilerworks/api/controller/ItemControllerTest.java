package com.boilerworks.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.boilerworks.api.TestConfig;
import com.boilerworks.api.model.Item;
import com.boilerworks.api.repository.CategoryRepository;
import com.boilerworks.api.repository.ItemRepository;
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
class ItemControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private ItemRepository itemRepository;

  @Autowired private CategoryRepository categoryRepository;

  @BeforeEach
  void setUp() {
    itemRepository.deleteAll();
    categoryRepository.deleteAll();
  }

  @Test
  @WithMockUser(authorities = {"item.add", "item.view"})
  void createItem() throws Exception {
    mockMvc
        .perform(
            post("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    objectMapper.writeValueAsString(
                        Map.of("name", "Widget", "price", "9.99", "sku", "WDG-001"))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(true))
        .andExpect(jsonPath("$.data.name").value("Widget"))
        .andExpect(jsonPath("$.data.slug").value("widget"));

    Item item = itemRepository.findByName("Widget").orElseThrow();
    assertThat(item.getPrice()).isEqualByComparingTo("9.99");
    assertThat(item.getSku()).isEqualTo("WDG-001");
  }

  @Test
  void createItemDeniedWithoutAuth() throws Exception {
    mockMvc
        .perform(
            post("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    objectMapper.writeValueAsString(
                        Map.of("name", "Widget", "price", "9.99", "sku", "WDG-001"))))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(authorities = {"item.view"})
  void createItemDeniedWithoutPermission() throws Exception {
    mockMvc
        .perform(
            post("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    objectMapper.writeValueAsString(
                        Map.of("name", "Widget", "price", "9.99", "sku", "WDG-001"))))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(authorities = {"item.view"})
  void listItems() throws Exception {
    Item p = new Item();
    p.setName("Test Item");
    p.setSlug("test-item");
    p.setPrice(BigDecimal.valueOf(19.99));
    p.setSku("TST-001");
    itemRepository.save(p);

    mockMvc
        .perform(get("/api/items"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name").value("Test Item"));
  }

  @Test
  @WithMockUser(authorities = {"item.view", "item.change"})
  void updateItem() throws Exception {
    Item p = new Item();
    p.setName("Old Name");
    p.setSlug("old-name");
    p.setPrice(BigDecimal.valueOf(10.00));
    p.setSku("OLD-001");
    p = itemRepository.save(p);

    mockMvc
        .perform(
            put("/api/items/" + p.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("name", "New Name"))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(true))
        .andExpect(jsonPath("$.data.name").value("New Name"));

    Item updated = itemRepository.findById(p.getId()).orElseThrow();
    assertThat(updated.getName()).isEqualTo("New Name");
    assertThat(updated.getSlug()).isEqualTo("new-name");
  }

  @Test
  @WithMockUser(authorities = {"item.view", "item.delete"})
  void softDeleteItem() throws Exception {
    Item p = new Item();
    p.setName("To Delete");
    p.setSlug("to-delete");
    p.setPrice(BigDecimal.valueOf(5.00));
    p.setSku("DEL-001");
    p = itemRepository.save(p);

    mockMvc
        .perform(delete("/api/items/" + p.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(true));

    // Soft-deleted: findAll should not return it (due to @SQLRestriction)
    assertThat(itemRepository.findAll()).isEmpty();
  }
}
