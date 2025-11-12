package io.github.fintrack.workspace.workspace.service;

import io.github.fintrack.auth.model.User;
import io.github.fintrack.auth.service.AuthService;
import io.github.fintrack.workspace.member.service.MemberService;
import io.github.fintrack.workspace.workspace.model.Type;
import io.github.fintrack.workspace.workspace.model.Workspace;
import io.github.fintrack.workspace.workspace.repository.WorkspaceRepository;
import io.github.fintrack.workspace.workspace.validator.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceValidator workspaceValidator;
    private final AuthService authService;
    private final MemberService memberService;

    public Optional<Workspace> findByIdAndDeletedAtIsNull(UUID id) {
        return workspaceRepository.findByIdAndDeletion_DeletedAtIsNull(id);
    }

    public List<Workspace> findAllByMembersUserAndDeletedAtIsNull(User user) {
        return workspaceRepository.findAllByMembers_UserAndDeletion_DeletedAtIsNull(user);
    }

    public void save(Workspace workspace) {
        workspaceRepository.save(workspace);
    }

    public void delete(Workspace workspace) {
        workspaceValidator.validToDelete(workspace);
        workspace.getDeletion().markAsDeleted(authService.getUserLoggedIn());
        this.save(workspace);
    }

    @Transactional
    public void createMain(User user) {
        String defaultNameMainWorkspace = "Principal";

        Workspace workspace = Workspace.builder()
                .name(defaultNameMainWorkspace)
                .type(Type.MAIN)
                .createdBy(user)
                .build();
        this.save(workspace);

        memberService.createOwner(user, workspace);

    }

    @Transactional
    public Workspace createOther(String Name, User user) {
        Workspace workspace = Workspace.builder()
                .name(Name)
                .type(Type.OTHER)
                .createdBy(user)
                .build();
        this.save(workspace);

        memberService.createOwner(user, workspace);

        return workspace;
    }
}
