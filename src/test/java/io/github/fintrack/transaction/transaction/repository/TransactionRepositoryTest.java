package io.github.fintrack.transaction.transaction.repository;

import io.github.fintrack.auth.model.Role;
import io.github.fintrack.auth.model.User;
import io.github.fintrack.auth.repository.UserRepository;
import io.github.fintrack.transaction.transaction.model.Transaction;
import io.github.fintrack.transaction.transaction.model.Type;
import io.github.fintrack.workspace.financial.category.model.Category;
import io.github.fintrack.workspace.financial.category.repository.CategoryRepository;
import io.github.fintrack.workspace.payment.method.model.Method;
import io.github.fintrack.workspace.payment.method.repository.MethodRepository;
import io.github.fintrack.workspace.workspace.model.Workspace;
import io.github.fintrack.workspace.workspace.repository.WorkspaceRepository;
import jakarta.persistence.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class TransactionRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MethodRepository methodRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private User user;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setName("Test User");
        user.setEmail("text@gmail.com");
        user.setPassword("123456");
        user.setRole(Role.USER);

        userRepository.save(user);

        Workspace workspace = new Workspace();
        workspace.setName("Test Workspace");
        workspace.setType(io.github.fintrack.workspace.workspace.model.Type.MAIN);
        workspace.getCreation().setCreatedBy(user);
        workspace.getCreation().setCreatedAt(LocalDateTime.now());

        workspaceRepository.save(workspace);

        Category category = new Category();
        category.setDescription("Test Category");
        category.setColor("#FFFFFF");
        category.setWorkspace(workspace);
        category.setType(Type.EXPENSE);
        category.getCreation().setCreatedBy(user);
        category.getCreation().setCreatedAt(LocalDateTime.now());

        categoryRepository.save(category);

        Method method = new Method();
        method.setDescription("Test Method");
        method.setType(io.github.fintrack.workspace.payment.method.model.Type.BANK_TRANSFER);
        method.setWorkspace(workspace);
        method.getCreation().setCreatedBy(user);
        method.getCreation().setCreatedAt(LocalDateTime.now());

        methodRepository.save(method);

        transaction = new Transaction();
        transaction.setType(Type.EXPENSE);
        transaction.setWorkspace(workspace);
        transaction.setCategory(category);
        transaction.setMethod(method);
        transaction.setDescription("Test Transaction");
        transaction.setAmount(BigDecimal.TEN);
        transaction.setAmountInstallment(BigDecimal.ONE);
        transaction.setStartDate(LocalDate.now());
        transaction.setTotalInstallment(10);
        transaction.setRecurrence(Boolean.FALSE);
        transaction.getCreation().setCreatedBy(user);
        transaction.getCreation().setCreatedAt(LocalDateTime.now());

        transactionRepository.save(transaction);
    }

    @Test
    @DisplayName("Should find by id and deleted At Is Null")
    void shouldFindByIdWhenNotDeleted() {
        var result = transactionRepository.findByIdAndDeletionDeletedAtIsNull(transaction.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getDescription()).isEqualTo(transaction.getDescription());
    }

    @Test
    @DisplayName("Should not find by id and deletedAt Is Not Null")
    void shouldNotFindByIdWhenDeletedAtIsNotNull() {
        transaction.getDeletion().markAsDeleted(user);
        transactionRepository.save(transaction);

        var result = transactionRepository.findByIdAndDeletionDeletedAtIsNull(transaction.getId());

        assertThat(result).isEmpty();
    }
}
