package com.boilerworks.api.controller;

import com.boilerworks.api.TestConfig;
import com.boilerworks.api.model.Category;
import com.boilerworks.api.repository.CategoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestConfig.class)
@Transactional
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        categoryRepository.deleteAll();
    }

    @Test
    @WithMockUser(authorities = {"category.add"})
    void createCategory() throws Exception {
        mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                    Map.of("name", "Electronics", "description", "Electronic devices"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.ok").value(true))
            .andExpect(jsonPath("$.data.name").value("Electronics"))
            .andExpect(jsonPath("$.data.slug").value("electronics"));

        assertThat(categoryRepository.findBySlug("electronics")).isPresent();
    }

    @Test
    @WithMockUser(authorities = {"category.view"})
    void listCategories() throws Exception {
        Category c = new Category();
        c.setName("Software");
        c.setSlug("software");
        c.setSortOrder(1);
        categoryRepository.save(c);

        mockMvc.perform(get("/api/categories"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("Software"));
    }

    @Test
    void listCategoriesDeniedWithoutAuth() throws Exception {
        mockMvc.perform(get("/api/categories"))
            .andExpect(status().isUnauthorized());
    }
}
