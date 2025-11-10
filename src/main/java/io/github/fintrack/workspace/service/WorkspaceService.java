package io.github.fintrack.workspace.service;

import io.github.fintrack.auth.service.AuthService;
import io.github.fintrack.workspace.model.Workspace;
import io.github.fintrack.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final AuthService authService;

    public void save(Workspace workspace) {
        workspaceRepository.save(workspace);
    }

    public Optional<Workspace> findById(UUID id) {
        return workspaceRepository.findById(id);
    }

    public void delete(Workspace workspace) {
        workspace.markAsDeleted(authService.getUserLoggedIn());
        this.save(workspace);
    }
}
