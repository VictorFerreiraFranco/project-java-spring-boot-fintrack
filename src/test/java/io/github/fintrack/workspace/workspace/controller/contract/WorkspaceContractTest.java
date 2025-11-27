package io.github.fintrack.workspace.workspace.controller.contract;

import io.github.fintrack.auth.model.User;
import io.github.fintrack.auth.service.AuthService;
import io.github.fintrack.workspace.workspace.controller.dto.WorkspaceDetailsResponse;
import io.github.fintrack.workspace.workspace.controller.dto.WorkspaceRequest;
import io.github.fintrack.workspace.workspace.controller.dto.WorkspaceSingleResponse;
import io.github.fintrack.workspace.workspace.controller.mapper.WorkspaceMapper;
import io.github.fintrack.workspace.workspace.model.Workspace;
import io.github.fintrack.workspace.workspace.service.WorkspaceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class WorkspaceContractTest {

    private final WorkspaceService workspaceService = mock(WorkspaceService.class);
    private final AuthService authService = mock(AuthService.class);
    private final WorkspaceMapper workspaceMapper = mock(WorkspaceMapper.class);

    private WorkspaceContract contract;

    private User user;
    private Workspace workspace;

    @BeforeEach
    void setup() {
        contract = new WorkspaceContract(workspaceService, authService, workspaceMapper);

        user = new User();
        user.setId(UUID.randomUUID());

        workspace = Workspace.builder()
                .name("Test")
                .build();

        workspace.setId(UUID.randomUUID());
    }


    @Test
    @DisplayName("Should return workspace details by id")
    void shouldReturnWorkspaceDetailsById() {
        WorkspaceDetailsResponse response = new WorkspaceDetailsResponse(
                workspace.getId(), workspace.getName(), LocalDateTime.now(), null, null
        );

        when(workspaceService.findByIdAndValidateExistenceAndMembership(workspace.getId()))
                .thenReturn(workspace);

        when(workspaceMapper.toDetailsResponse(workspace))
                .thenReturn(response);

        WorkspaceDetailsResponse result = contract.getById(workspace.getId().toString());

        assertThat(result).isEqualTo(response);
        verify(workspaceService).findByIdAndValidateExistenceAndMembership(workspace.getId());
        verify(workspaceMapper).toDetailsResponse(workspace);
    }

    @Test
    @DisplayName("Should return workspace list for logged user")
    void shouldReturnWorkspaceListForLoggedUser() {
        WorkspaceDetailsResponse resp = new WorkspaceDetailsResponse(
                workspace.getId(), workspace.getName(),  LocalDateTime.now(), null, null
        );

        when(authService.getUserLoggedIn())
                .thenReturn(user);

        when(workspaceService.findAllByMembersUserAndDeletedAtIsNull(user))
                .thenReturn(List.of(workspace));

        when(workspaceMapper.toDetailsResponse(workspace))
                .thenReturn(resp);

        List<WorkspaceDetailsResponse> result = contract.getByUserLoggedId();

        assertThat(result)
                .hasSize(1)
                .contains(resp);
    }

    @Test
    @DisplayName("Should create workspace")
    void shouldCreateWorkspace() {
        WorkspaceRequest request = new WorkspaceRequest("New Workspace");

        Workspace saved = Workspace.builder()
                .name("New Workspace")
                .build();
        saved.setId(UUID.randomUUID());

        WorkspaceSingleResponse response = new WorkspaceSingleResponse(
                saved.getId(), saved.getName(), null
        );

        when(authService.getUserLoggedIn())
                .thenReturn(user);

        when(workspaceService.createOther("New Workspace", user))
                .thenReturn(saved);

        when(workspaceMapper.toSingleResponse(saved))
                .thenReturn(response);

        WorkspaceSingleResponse result = contract.create(request);

        assertThat(result).isEqualTo(response);
    }

    @Test
    @DisplayName("Should update workspace")
    void shouldUpdateWorkspace() {
        UUID id = UUID.randomUUID();
        WorkspaceRequest request = new WorkspaceRequest("Updated Name");

        Workspace mapped = Workspace.builder().name("Updated Name").build();

        Workspace updated = Workspace.builder()
                .name("Updated Name")
                .build();
        updated.setId(id);

        WorkspaceSingleResponse response = new WorkspaceSingleResponse(id, "Updated Name", null);

        when(workspaceMapper.toEntity(request))
                .thenReturn(mapped);

        when(workspaceService.findByIdAndValidateExistenceAndMembership(id))
                .thenReturn(workspace); // existing

        when(workspaceService.save(workspace))
                .thenReturn(updated);

        when(workspaceMapper.toSingleResponse(updated))
                .thenReturn(response);

        WorkspaceSingleResponse result = contract.update(id.toString(), request);

        assertThat(result).isEqualTo(response);
        assertThat(workspace.getName()).isEqualTo("Updated Name");
    }

    @Test
    @DisplayName("Should delete workspace")
    void shouldDeleteWorkspace() {
        UUID id = UUID.randomUUID();

        when(workspaceService.findByIdAndValidateExistenceAndMembership(id))
                .thenReturn(workspace);

        contract.delete(id.toString());

        verify(workspaceService).delete(workspace);
    }
}