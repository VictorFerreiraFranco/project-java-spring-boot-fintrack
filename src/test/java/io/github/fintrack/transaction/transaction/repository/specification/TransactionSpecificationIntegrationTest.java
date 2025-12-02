package io.github.fintrack.transaction.transaction.repository.specification;

import io.github.fintrack.auth.model.Role;
import io.github.fintrack.auth.model.User;
import io.github.fintrack.auth.repository.UserRepository;
import io.github.fintrack.transaction.installment.model.Installment;
import io.github.fintrack.transaction.installment.repository.InstallmentRepository;
import io.github.fintrack.transaction.transaction.model.Transaction;
import io.github.fintrack.transaction.transaction.model.Type;
import io.github.fintrack.transaction.transaction.repository.TransactionRepository;
import io.github.fintrack.workspace.financial.category.model.Category;
import io.github.fintrack.workspace.financial.category.repository.CategoryRepository;
import io.github.fintrack.workspace.payment.method.model.Method;
import io.github.fintrack.workspace.payment.method.repository.MethodRepository;
import io.github.fintrack.workspace.workspace.model.Workspace;
import io.github.fintrack.workspace.workspace.repository.WorkspaceRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class TransactionSpecificationIntegrationTest {

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

    @Autowired
    private InstallmentRepository installmentRepository;

    private User user;
    private Workspace workspace;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setName("Test User");
        user.setEmail("text@gmail.com");
        user.setPassword("123456");
        user.setRole(Role.USER);
        userRepository.save(user);

        workspace = new Workspace();
        workspace.setName("Test Workspace");
        workspace.setType(io.github.fintrack.workspace.workspace.model.Type.MAIN);
        workspace.getCreation().setCreatedBy(user);
        workspace.getCreation().setCreatedAt(LocalDateTime.now());
        workspaceRepository.save(workspace);

        Category category = new Category();
        category.setDescription("Test Category");
        category.setColor("#FFF");
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
        transaction.setWorkspace(workspace);
        transaction.setCategory(category);
        transaction.setMethod(method);
        transaction.setType(Type.EXPENSE);
        transaction.setDescription("Test Transaction");
        transaction.setAmount(BigDecimal.TEN);
        transaction.setAmountInstallment(BigDecimal.ONE);
        transaction.setStartDate(LocalDate.of(2024, 1, 1));
        transaction.setTotalInstallment(10);
        transaction.setRecurrence(false);
        transaction.getCreation().setCreatedBy(user);
        transaction.getCreation().setCreatedAt(LocalDateTime.now());
        transactionRepository.save(transaction);
    }

    @Test
    @DisplayName("deletedAtIsNull: should return transaction when not deleted")
    void shouldReturnTransactionWhenNotDeleted() {
        var spec = TransactionSpecification.deletedAtIsNull();

        var results = transactionRepository.findAll(spec);

        assertThat(results).hasSize(1);
        assertThat(results.getFirst().getId()).isEqualTo(transaction.getId());
    }

    @Test
    @DisplayName("deletedAtIsNull: should not return deleted transaction")
    void shouldNotReturnWhenDeleted() {
        transaction.getDeletion().markAsDeleted(user);
        transactionRepository.save(transaction);

        var spec = TransactionSpecification.deletedAtIsNull();
        var results = transactionRepository.findAll(spec);

        assertThat(results).isEmpty();
    }

    @Test
    @DisplayName("endDateIsNull: should return transaction with null endDate")
    void shouldReturnWhenEndDateIsNull() {
        var spec = TransactionSpecification.endDateIsNull();
        var results = transactionRepository.findAll(spec);

        assertThat(results).hasSize(1);
    }

    @Test
    @DisplayName("workspaceEqual: should return only transactions from workspace")
    void shouldMatchWorkspace() {
        var spec = TransactionSpecification.workspaceEqual(workspace);

        var results = transactionRepository.findAll(spec);

        assertThat(results).hasSize(1);
        assertThat(results.getFirst().getWorkspace()).isEqualTo(workspace);
    }

    @Test
    @DisplayName("startDateThanOrEqual: should filter startDate correctly")
    void shouldFilterByStartDate() {
        var spec = TransactionSpecification.startDateThanOrEqual(LocalDate.of(2023, 12, 31));

        var results = transactionRepository.findAll(spec);

        assertThat(results).hasSize(1);
    }
}
