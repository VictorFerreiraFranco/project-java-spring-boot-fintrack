package io.github.fintrack.workspace.payment.method.service;

import io.github.fintrack.auth.service.AuthService;
import io.github.fintrack.workspace.payment.method.controller.dto.MethodFilter;
import io.github.fintrack.workspace.payment.method.exception.PaymentMethodNotFoundException;
import io.github.fintrack.workspace.payment.method.model.Method;
import io.github.fintrack.workspace.payment.method.repository.MethodRepository;
import io.github.fintrack.workspace.payment.method.repository.specification.MethodSpecification;
import io.github.fintrack.workspace.payment.method.service.validator.MethodValidator;
import io.github.fintrack.workspace.workspace.model.Workspace;
import io.github.fintrack.workspace.workspace.service.validator.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MethodService {

    private final MethodRepository methodRepository;
    private final MethodValidator methodValidator;
    private final AuthService authService;
    private final WorkspaceValidator workspaceValidator;

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

    public Method findByIdAndValidateExistence(UUID id) {
        return this.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(PaymentMethodNotFoundException::new);
    }


    @Transactional
    public Method findByIdAndValidateExistenceAndMembershipByWorkspace(UUID id) {
        Method method = this.findByIdAndValidateExistence(id);

        workspaceValidator.validUserLoggedInIsMemberByWorkspace(method.getWorkspace());

        return method;
    }

    @Transactional
    public Method save(Method method) {
        methodValidator.validToSave(method);
        return methodRepository.save(method);
    }

    @Transactional
    public void delete(Method method) {
        methodValidator.validToDelete(method);
        method.getDeletion().markAsDeleted(authService.getUserLoggedIn());
        methodRepository.save(method);
    }
}
