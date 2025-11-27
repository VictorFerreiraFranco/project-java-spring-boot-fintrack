package io.github.fintrack.workspace.workspace.service;


import io.github.fintrack.auth.model.User;
import io.github.fintrack.auth.service.AuthService;
import io.github.fintrack.workspace.member.service.MemberService;
import io.github.fintrack.workspace.workspace.exception.WorkspaceNotFoundException;
import io.github.fintrack.workspace.workspace.model.Type;
import io.github.fintrack.workspace.workspace.model.Workspace;
import io.github.fintrack.workspace.workspace.repository.WorkspaceRepository;
import io.github.fintrack.workspace.workspace.service.validator.WorkspaceValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class WorkspaceServiceTest {

    private final WorkspaceRepository workspaceRepository = mock(WorkspaceRepository.class);
    private final WorkspaceValidator workspaceValidator = mock(WorkspaceValidator.class);
    private final AuthService authService = mock(AuthService.class);
    private final MemberService memberService = mock(MemberService.class);

    private final WorkspaceService workspaceService = new WorkspaceService(
            workspaceRepository, workspaceValidator, authService, memberService
    );

    @Test
    @DisplayName("Should find by id and deletedAt is null")
    void shouldFindByIdAndDeletedAtIsNull() {

        Workspace workspace = new Workspace();
        workspace.setId(UUID.randomUUID());

        when(workspaceRepository.findByIdAndDeletion_DeletedAtIsNull(workspace.getId()))
                .thenReturn(Optional.of(workspace));

        Optional<Workspace> result = workspaceService.findByIdAndDeletedAtIsNull(workspace.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(workspace.getId());

        verify(workspaceRepository).findByIdAndDeletion_DeletedAtIsNull(workspace.getId());
    }

    @Test
    @DisplayName("Should find all by members user and deletedAt is null")
    void shouldFindAllByMembersUserAndDeletedAtIsNull() {
        User user = new User();
        List<Workspace> expectedList = List.of(new Workspace(), new Workspace());

        when(workspaceRepository.findAll(any(Specification.class)))
                .thenReturn(expectedList);

        List<Workspace> result = workspaceService.findAllByMembersUserAndDeletedAtIsNull(user);

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Should return workspace when exists and user is member")
    void shouldReturnWorkspaceWhenExistsAndUserIsMember() {
        UUID id = UUID.randomUUID();
        Workspace workspace = new Workspace();

        when(workspaceRepository.findByIdAndDeletion_DeletedAtIsNull(id))
                .thenReturn(Optional.of(workspace));

        Workspace result = workspaceService.findByIdAndValidateExistenceAndMembership(id);

        assertThat(result).isEqualTo(workspace);
        verify(workspaceValidator).validUserLoggedInIsMemberByWorkspace(workspace);
    }

    @Test
    @DisplayName("Should throw exception when workspace not found")
    void shouldThrowExceptionWhenWorkspaceNotFound() {
        UUID id = UUID.randomUUID();

        when(workspaceRepository.findByIdAndDeletion_DeletedAtIsNull(id))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                workspaceService.findByIdAndValidateExistenceAndMembership(id)
        ).isInstanceOf(WorkspaceNotFoundException.class);
    }

    @Test
    @DisplayName("Should save workspace")
    void shouldSaveWorkspace() {
        Workspace workspace = new Workspace();
        when(workspaceRepository.save(workspace)).thenReturn(workspace);

        Workspace result = workspaceService.save(workspace);

        assertThat(result).isEqualTo(workspace);
    }

    @Test
    @DisplayName("Should delete workspace")
    void shouldDeleteWorkspace() {
        User loggedUser = new User();

        Workspace workspace = new Workspace();
        workspace.getDeletion().markAsDeleted(loggedUser);

        when(authService.getUserLoggedIn()).thenReturn(loggedUser);
        when(workspaceRepository.save(workspace)).thenReturn(workspace);

        workspaceService.delete(workspace);

        verify(workspaceValidator).validToDelete(workspace);
        assertThat(workspace.getDeletion().getDeletedAt()).isNotNull();
        verify(workspaceRepository).save(workspace);
    }

    @Test
    @DisplayName("Should create main workspace and create owner")
    void shouldCreateMainWorkspaceAndCreateOwner() {
        User user = new User();
        ArgumentCaptor<Workspace> captor = ArgumentCaptor.forClass(Workspace.class);

        when(workspaceRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        workspaceService.createMain(user);

        verify(workspaceRepository).save(captor.capture());
        Workspace created = captor.getValue();

        assertThat(created.getName()).isEqualTo("Principal");
        assertThat(created.getType()).isEqualTo(Type.MAIN);
        assertThat(created.getCreation().getCreatedBy()).isEqualTo(user);

        verify(memberService).createOwner(user, created);
    }


    @Test
    @DisplayName("Should create other workspace")
    void shouldCreateOtherWorkspace() {
        User user = new User();
        String name = "Financeiro";

        when(workspaceRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Workspace result = workspaceService.createOther(name, user);

        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getType()).isEqualTo(Type.OTHER);
        assertThat(result.getCreation().getCreatedBy()).isEqualTo(user);

        verify(memberService).createOwner(user, result);
    }
}
