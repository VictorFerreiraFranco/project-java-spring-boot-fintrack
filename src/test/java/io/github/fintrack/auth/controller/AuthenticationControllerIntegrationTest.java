package io.github.fintrack.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.fintrack.auth.controller.contract.AuthenticationContract;
import io.github.fintrack.auth.controller.dto.*;
import io.github.fintrack.auth.model.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(AuthenticationController.class)
public class AuthenticationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthenticationContract contract;

    @Test
    @DisplayName("POST /register should return authentication response")
    void registerShouldReturnOk() throws Exception {
        RegisterRequest request = new RegisterRequest(
                "Test Name", "test@email.com", "123456"
        );

        AuthenticationResponse response = new AuthenticationResponse(
                "access", "refresh"
        );

        when(contract.register(request))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value(response.accessToken()))
                .andExpect(jsonPath("$.refresh_token").value(response.refreshToken()));

    }

    @Test
    @DisplayName("POST /authenticate should return authentication response")
    void authenticateTest() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest(
                "test@email.com", "123456"
        );

        AuthenticationResponse response = new AuthenticationResponse(
                "access-token", "refresh-token"
        );

        when(contract.authenticate(any(AuthenticationRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value(response.accessToken()))
                .andExpect(jsonPath("$.refresh_token").value(response.refreshToken()));
    }

    @Test
    @DisplayName("POST /refresh-token should return authentication response")
    void refreshTokenTest() throws Exception {
        TokenRefreshRequest request = new TokenRefreshRequest("refresh-token");

        AuthenticationResponse response = new AuthenticationResponse(
                "access-token", "refresh-token"
        );

        when(contract.refreshToken(any(TokenRefreshRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value(response.accessToken()))
                .andExpect(jsonPath("$.refresh_token").value(response.refreshToken()));
    }

    @Test
    @DisplayName("GET /me should return current user")
    void getCurrentUserTest() throws Exception {
        UserResponse userResponse = new UserResponse(UUID.randomUUID(), "Test User", "test@email.com", Role.USER);

        when(contract.getCurrentUser()).thenReturn(userResponse);

        mockMvc.perform(get("/api/v1/auth/me")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userResponse.id().toString()))
                .andExpect(jsonPath("$.name").value(userResponse.name()))
                .andExpect(jsonPath("$.email").value(userResponse.email()))
                .andExpect(jsonPath("$.role").value(userResponse.role().name()));
    }
}
