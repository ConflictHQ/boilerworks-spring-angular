package com.boilerworks.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.boilerworks.api.TestConfig;
import com.boilerworks.api.model.AppUser;
import com.boilerworks.api.model.Permission;
import com.boilerworks.api.model.UserGroup;
import com.boilerworks.api.repository.AppUserRepository;
import com.boilerworks.api.repository.PermissionRepository;
import com.boilerworks.api.repository.UserGroupRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestConfig.class)
@Transactional
class AuthControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private AppUserRepository userRepository;

  @Autowired private UserGroupRepository groupRepository;

  @Autowired private PermissionRepository permissionRepository;

  @Autowired private PasswordEncoder passwordEncoder;

  @BeforeEach
  void setUp() {
    userRepository.deleteAll();
    groupRepository.deleteAll();
    permissionRepository.deleteAll();

    Permission viewPerm = new Permission("product.view", "View products", null);
    permissionRepository.save(viewPerm);

    UserGroup group = new UserGroup("Administrators", "Full access");
    group.setPermissions(new HashSet<>(Set.of(viewPerm)));
    groupRepository.save(group);

    AppUser user = new AppUser();
    user.setEmail("test@boilerworks.dev");
    user.setPassword(passwordEncoder.encode("password123"));
    user.setFirstName("Test");
    user.setLastName("User");
    user.setActive(true);
    user.setStaff(true);
    user.setGroups(new HashSet<>(Set.of(group)));
    userRepository.save(user);
  }

  @Test
  void loginSuccessful() throws Exception {
    mockMvc
        .perform(
            post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    objectMapper.writeValueAsString(
                        Map.of("email", "test@boilerworks.dev", "password", "password123"))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(true))
        .andExpect(jsonPath("$.data.email").value("test@boilerworks.dev"))
        .andExpect(jsonPath("$.data.firstName").value("Test"));
  }

  @Test
  void loginWithBadCredentials() throws Exception {
    mockMvc
        .perform(
            post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    objectMapper.writeValueAsString(
                        Map.of("email", "test@boilerworks.dev", "password", "wrong"))))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void meRequiresAuthentication() throws Exception {
    mockMvc.perform(get("/api/auth/me")).andExpect(status().isUnauthorized());
  }
}
