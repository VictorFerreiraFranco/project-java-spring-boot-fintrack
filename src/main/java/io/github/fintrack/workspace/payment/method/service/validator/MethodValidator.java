package io.github.fintrack.workspace.payment.method.service.validator;

import io.github.fintrack.common.exception.DuplicateRecordException;
import io.github.fintrack.workspace.payment.method.controller.dto.MethodFilter;
import io.github.fintrack.workspace.payment.method.model.Method;
import io.github.fintrack.workspace.payment.method.repository.MethodRepository;
import io.github.fintrack.workspace.payment.method.repository.specification.MethodSpecification;
import io.github.fintrack.workspace.workspace.service.validator.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MethodValidator {

    private final MethodRepository methodRepository;
    private final WorkspaceValidator workspaceValidator;

    public void validToSave(Method method) {
        if (methodExists(method))
            throw new DuplicateRecordException("Method already exists");

        workspaceValidator.validUserLoggedInIsMemberByWorkspace(method.getWorkspace());
    }

    public void validToDelete(Method method) {
        workspaceValidator.validUserLoggedInIsMemberByWorkspace(method.getWorkspace());
    }

    public boolean methodExists(Method method) {
        List<Method> methodFund = methodRepository.findAll(
                MethodSpecification.deletedAtIsNull()
                        .and(MethodSpecification.workspaceEqual(method.getWorkspace()))
                        .and(MethodSpecification.applyFilter(new MethodFilter(
                                method.getDescription(),
                                method.getType().toString()
                        )))
        );

        if (method.getId() == null)
            return !methodFund.isEmpty();

        return !methodFund.isEmpty() && !methodFund.getFirst().getId().equals(method.getId());
    }
}
