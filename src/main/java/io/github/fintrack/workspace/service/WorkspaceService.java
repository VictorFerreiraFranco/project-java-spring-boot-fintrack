package io.github.fintrack.workspace.service;

import io.github.fintrack.auth.service.AuthService;
import io.github.fintrack.workspace.model.Workspace;
import io.github.fintrack.workspace.repository.WorkspaceRepository;
import io.github.fintrack.workspace.validator.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final AuthService authService;
    private final WorkspaceValidator workspaceValidator;

    public void save(Workspace workspace) {
        workspaceRepository.save(workspace);
    }

    public Optional<Workspace> findById(UUID id) {
        return workspaceRepository.findById(id);
    }

    public Optional<Workspace> findByIdAndDeletedAtIsNull(UUID id) {
        return workspaceRepository.findByIdAndDeletion_DeletedAtIsNull(id);
    }

    public void delete(Workspace workspace) {
        workspaceValidator.validateDelete(workspace);
        workspace.getDeletion().markAsDeleted(authService.getUserLoggedIn());
        this.save(workspace);
    }
}
