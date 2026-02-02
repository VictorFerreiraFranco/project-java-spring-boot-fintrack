package io.github.fintrack.workspace.payment.method.service;

import io.github.fintrack.auth.model.User;
import io.github.fintrack.auth.service.AuthService;
import io.github.fintrack.workspace.payment.method.controller.dto.MethodFilter;
import io.github.fintrack.workspace.payment.method.exception.PaymentMethodNotFoundException;
import io.github.fintrack.workspace.payment.method.model.Method;
import io.github.fintrack.workspace.payment.method.model.Type;
import io.github.fintrack.workspace.payment.method.repository.MethodRepository;
import io.github.fintrack.workspace.payment.method.service.validator.MethodValidator;
import io.github.fintrack.workspace.workspace.model.Workspace;
import io.github.fintrack.workspace.workspace.service.validator.WorkspaceValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class MethodServiceTest {

    private final MethodRepository methodRepository = mock(MethodRepository.class);
    private final MethodValidator methodValidator = mock(MethodValidator.class);
    private final AuthService authService = mock(AuthService.class);
    private final WorkspaceValidator workspaceValidator = mock(WorkspaceValidator.class);

    private final MethodService methodService = new MethodService(
            methodRepository, methodValidator, authService, workspaceValidator
    );

    @Test
    @DisplayName("Should find by id and deletedAt is null")
    void shouldFindByIdAndDeletedAtIsNull() {
        UUID id = UUID.randomUUID();
        Method method = new Method();
        method.setId(id);

        when(methodRepository.findByIdAndDeletionDeletedAtIsNull(id))
                .thenReturn(Optional.of(method));

        Optional<Method> result = methodService.findByIdAndDeletedAtIsNull(id);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(id);

        verify(methodRepository).findByIdAndDeletionDeletedAtIsNull(id);
    }

    @Test
    @DisplayName("Should search methods by workspace and filter")
    void shouldSearchMethodsByWorkspaceAndFilter() {
        Workspace workspace = new Workspace();
        MethodFilter filter = new MethodFilter(Type.DEBIT_CARD.toString(), "");

        List<Method> expected = List.of(new Method(), new Method());

        when(methodRepository.findAll(any(Specification.class)))
                .thenReturn(expected);

        List<Method> result = methodService.searchByWorkspace(workspace, filter);

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Should return method when exists")
    void shouldReturnMethodWhenExists() {
        UUID id = UUID.randomUUID();
        Method method = new Method();
        method.setId(id);

        when(methodRepository.findByIdAndDeletionDeletedAtIsNull(id))
                .thenReturn(Optional.of(method));

        Method result = methodService.findByIdAndValidateExistence(id);

        assertThat(result).isEqualTo(method);
    }

    @Test
    @DisplayName("Should throw exception when method does not exist")
    void shouldThrowExceptionWhenMethodDoesNotExist() {
        UUID id = UUID.randomUUID();

        when(methodRepository.findByIdAndDeletionDeletedAtIsNull(id))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                methodService.findByIdAndValidateExistence(id)
        ).isInstanceOf(PaymentMethodNotFoundException.class);
    }

    @Test
    @DisplayName("Should return method and validate membership")
    void shouldReturnMethodAndValidateMembership() {
        UUID id = UUID.randomUUID();
        Workspace workspace = new Workspace();

        Method method = new Method();
        method.setWorkspace(workspace);

        when(methodRepository.findByIdAndDeletionDeletedAtIsNull(id))
                .thenReturn(Optional.of(method));

        Method result = methodService.findByIdAndValidateExistenceAndMembershipByWorkspace(id);

        assertThat(result).isEqualTo(method);
        verify(workspaceValidator).validUserLoggedInIsMemberByWorkspace(workspace);
    }

    @Test
    @DisplayName("Should save method")
    void shouldSaveMethod() {
        Method method = new Method();

        when(methodRepository.save(method))
                .thenReturn(method);

        Method result = methodService.save(method);

        verify(methodValidator).validToSave(method);
        assertThat(result).isEqualTo(method);
    }

    @Test
    @DisplayName("Should delete method")
    void shouldDeleteMethod() {
        User user = new User();
        Method method = new Method();

        // marca como deletado dentro da execução
        when(authService.getUserLoggedIn()).thenReturn(user);
        when(methodRepository.save(method)).thenReturn(method);

        methodService.delete(method);

        verify(methodValidator).validToDelete(method);
        assertThat(method.getDeletion().getDeletedAt()).isNotNull();
        assertThat(method.getDeletion().getDeletedBy()).isEqualTo(user);

        verify(methodRepository).save(method);
    }
}
