package io.github.fintrack.workspace.payment.method.service;

import io.github.fintrack.auth.service.AuthService;
import io.github.fintrack.workspace.payment.method.controller.dto.MethodFilter;
import io.github.fintrack.workspace.payment.method.model.Method;
import io.github.fintrack.workspace.payment.method.repository.MethodRepository;
import io.github.fintrack.workspace.payment.method.repository.specification.MethodSpecification;
import io.github.fintrack.workspace.payment.method.validator.MethodValidator;
import io.github.fintrack.workspace.workspace.model.Workspace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MethodService {

    private final MethodRepository methodRepository;
    private final MethodValidator methodValidator;
    private final AuthService authService;

    public Optional<Method> findByIdAndDeletedAtIsNull(UUID id) {
        return methodRepository.findByIdAndDeletion_DeletedAtIsNull(id);
    }

    public List<Method> searchByWorkspace(
            Workspace workspace, MethodFilter filter
    ) {
        return methodRepository.findAll(
                MethodSpecification.deletedAtIsNull()
                        .and(MethodSpecification.workspaceEqual(workspace))
                        .and(MethodSpecification.applyFilter(filter))
        );
    }

    public Method save(Method method) {
        methodValidator.validToSave(method);
        return methodRepository.save(method);
    }

    public void delete(Method method) {
        methodValidator.validToDelete(method);
        method.getDeletion().markAsDeleted(authService.getUserLoggedIn());
        methodRepository.save(method);
    }
}
