package io.github.fintrack.workspace.workspace.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.fintrack.workspace.workspace.controller.contract.WorkspaceContract;
import io.github.fintrack.workspace.workspace.controller.dto.WorkspaceDetailsResponse;
import io.github.fintrack.workspace.workspace.controller.dto.WorkspaceRequest;
import io.github.fintrack.workspace.workspace.controller.dto.WorkspaceSingleResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.endsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(WorkspaceController.class)
class WorkspaceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WorkspaceContract contract;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @DisplayName("GET /{id} deve retornar detalhes do workspace")
    void getById_shouldReturnWorkspaceDetails() throws Exception {
        String id = UUID.randomUUID().toString();
        WorkspaceDetailsResponse response = new WorkspaceDetailsResponse(
                UUID.fromString(id),
                "Test Workspace",
                LocalDateTime.now(),
                null,
                List.of()
        );

        Mockito.when(contract.getById(id)).thenReturn(response);

        mockMvc.perform(get("/api/v1/workspaces/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Test Workspace"));
    }

    @Test
    @DisplayName("GET /my deve retornar lista de workspaces do usuário logado")
    void getByUserLoggedId_shouldReturnList() throws Exception {
        WorkspaceDetailsResponse w1 = new WorkspaceDetailsResponse(
                UUID.randomUUID(),
                "Workspace 1",
                LocalDateTime.now(),
                null,
                List.of()
        );

        WorkspaceDetailsResponse w2 = new WorkspaceDetailsResponse(
                UUID.randomUUID(),
                "Workspace 2",
                LocalDateTime.now(),
                null,
                List.of()
        );

        Mockito.when(contract.getByUserLoggedId()).thenReturn(List.of(w1, w2));

        mockMvc.perform(get("/api/v1/workspaces/my"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("POST deve criar workspace e retornar Location + corpo")
    void create_shouldReturnCreated() throws Exception {
        String id = UUID.randomUUID().toString();

        WorkspaceRequest request = new WorkspaceRequest("New Workspace");

        WorkspaceSingleResponse response = new WorkspaceSingleResponse(
                UUID.fromString(id),
                "New Workspace",
                LocalDateTime.now()
        );

        Mockito.when(contract.create(any())).thenReturn(response);

        mockMvc.perform(
                        post("/api/v1/workspaces")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", endsWith("/api/v1/workspaces/" + id)))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("New Workspace"));
    }

    @Test
    @DisplayName("PATCH /{id} deve atualizar workspace")
    void update_shouldReturnUpdated() throws Exception {
        String id = UUID.randomUUID().toString();

        WorkspaceRequest request = new WorkspaceRequest("Updated Workspace");

        WorkspaceSingleResponse response = new WorkspaceSingleResponse(
                UUID.fromString(id),
                "Updated Workspace",
                LocalDateTime.now()
        );

        Mockito.when(contract.update(eq(id), any())).thenReturn(response);

        mockMvc.perform(
                        patch("/api/v1/workspaces/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Updated Workspace"));
    }

    @Test
    @DisplayName("DELETE /{id} deve deletar e retornar 204")
    void delete_shouldReturnNoContent() throws Exception {
        String id = UUID.randomUUID().toString();

        mockMvc.perform(delete("/api/v1/workspaces/{id}", id))
                .andExpect(status().isNoContent());

        Mockito.verify(contract).delete(id);
    }
}
