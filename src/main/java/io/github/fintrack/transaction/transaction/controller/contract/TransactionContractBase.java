package io.github.fintrack.transaction.transaction.controller.contract;

import io.github.fintrack.transaction.transaction.controller.dto.TransactionResponse;
import io.github.fintrack.transaction.transaction.controller.mapper.TransactionMapper;
import io.github.fintrack.transaction.transaction.exception.CategoryInvalidForTypeException;
import io.github.fintrack.transaction.transaction.exception.TransactionNotFoundException;
import io.github.fintrack.transaction.transaction.model.Transaction;
import io.github.fintrack.transaction.transaction.model.Type;
import io.github.fintrack.transaction.transaction.service.TransactionService;
import io.github.fintrack.workspace.financial.category.exception.CategoryNotFoundException;
import io.github.fintrack.workspace.financial.category.exception.CategoryNotFoundInWorkspaceException;
import io.github.fintrack.workspace.financial.category.model.Category;
import io.github.fintrack.workspace.financial.category.service.CategoryService;
import io.github.fintrack.workspace.member.exception.MemberNotFoundException;
import io.github.fintrack.workspace.member.exception.MemberNotFoundInWorkspaceException;
import io.github.fintrack.workspace.payment.method.model.Method;
import io.github.fintrack.workspace.payment.method.service.MethodService;
import io.github.fintrack.workspace.workspace.exception.WorkspaceNotFoundException;
import io.github.fintrack.workspace.workspace.model.Workspace;
import io.github.fintrack.workspace.workspace.service.WorkspaceService;
import io.github.fintrack.workspace.workspace.service.validator.WorkspaceValidator;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public abstract class TransactionContractBase {

    protected final WorkspaceService workspaceService;
    protected final WorkspaceValidator workspaceValidator;
    protected final TransactionService transactionService;
    protected final CategoryService categoryService;
    protected final MethodService methodService;
    private final TransactionMapper transactionMapper;

    protected TransactionContractBase(
            WorkspaceService workspaceService,
            WorkspaceValidator workspaceValidator,
            TransactionService transactionService,
            CategoryService categoryService,
            MethodService methodService,
            TransactionMapper transactionMapper
    ) {
        this.workspaceService = workspaceService;
        this.workspaceValidator = workspaceValidator;
        this.transactionService = transactionService;
        this.categoryService = categoryService;
        this.methodService = methodService;
        this.transactionMapper = transactionMapper;
    }

    @Transactional(readOnly = true)
    protected Transaction findById(String id) {
        Transaction method = transactionService.findByIdAndDeleteAtIsNull(UUID.fromString(id))
                .orElseThrow(TransactionNotFoundException::new);

        workspaceValidator.validUserLoggedInIsMemberByWorkspace(method.getWorkspace());

        return method;
    }

    @Transactional(readOnly = true)
    protected Workspace findWorkspaceById(String workspaceId) {
        Workspace workspace = workspaceService.findByIdAndDeletedAtIsNull(UUID.fromString(workspaceId))
                .orElseThrow(WorkspaceNotFoundException::new);

        workspaceValidator.validUserLoggedInIsMemberByWorkspace(workspace);

        return workspace;
    }

    @Transactional(readOnly = true)
    protected Category findCategoryByIdWithWorkspaceAndType(
            String categoryId, Workspace workspace, Type type
            ) {
        Category category = categoryService.findByIdAndDeletedAtIsNull(UUID.fromString(categoryId))
                .orElseThrow(CategoryNotFoundException::new);

        if (!category.getWorkspace().getId().equals(workspace.getId()))
            throw new CategoryNotFoundInWorkspaceException();

        if (!category.getType().equals(type))
            throw new CategoryInvalidForTypeException();

        return category;
    }

    @Transactional(readOnly = true)
    protected Method findMethodByIdWithWorkspace(String methodId, Workspace workspace) {
        Method method = methodService.findByIdAndDeletedAtIsNull(UUID.fromString(methodId))
                .orElseThrow(MemberNotFoundException::new);

        if (!method.getWorkspace().getId().equals(workspace.getId()))
            throw new MemberNotFoundInWorkspaceException();

        return method;
    }

    @Transactional
    public TransactionResponse create(
            Transaction transaction,
            String workspaceId,
            String categoryId,
            String methodId
    ) {
        Workspace workspace = this.findWorkspaceById(workspaceId);

        transaction.setWorkspace(workspace);
        transaction.setCategory(this.findCategoryByIdWithWorkspaceAndType(categoryId, workspace, transaction.getType()));
        transaction.setMethod(this.findMethodByIdWithWorkspace(methodId, workspace));

        transactionService.save(transaction);
        return transactionMapper.toResponse(transaction);
    }

    @Transactional
    public TransactionResponse update(
            Transaction transaction,
            String categoryId,
            String methodId
    ) {
        transaction.setCategory(this.findCategoryByIdWithWorkspaceAndType(categoryId, transaction.getWorkspace(), transaction.getType()));
        transaction.setMethod(this.findMethodByIdWithWorkspace(methodId, transaction.getWorkspace()));

        transactionService.save(transaction);
        return transactionMapper.toResponse(transaction);
    }
}
