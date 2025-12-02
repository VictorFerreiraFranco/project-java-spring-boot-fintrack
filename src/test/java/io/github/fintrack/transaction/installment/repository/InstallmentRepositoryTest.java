package io.github.fintrack.transaction.installment.repository;

import io.github.fintrack.auth.model.Role;
import io.github.fintrack.auth.model.User;
import io.github.fintrack.auth.repository.UserRepository;
import io.github.fintrack.transaction.installment.model.Installment;
import io.github.fintrack.transaction.transaction.model.Transaction;
import io.github.fintrack.transaction.transaction.model.Type;
import io.github.fintrack.transaction.transaction.repository.TransactionRepository;
import io.github.fintrack.workspace.financial.category.model.Category;
import io.github.fintrack.workspace.financial.category.repository.CategoryRepository;
import io.github.fintrack.workspace.payment.method.model.Method;
import io.github.fintrack.workspace.payment.method.repository.MethodRepository;
import io.github.fintrack.workspace.workspace.model.Workspace;
import io.github.fintrack.workspace.workspace.repository.WorkspaceRepository;
import org.assertj.core.api.Assertions;
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

@DataJpaTest
@ActiveProfiles("test")
public class InstallmentRepositoryTest {

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

    private Transaction transaction;

    @BeforeEach
    void setUp() {
        User user = new User();
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

        LocalDate date = LocalDate.of(2025, 1, 1);

        for (int month = 0; month < 12; month++) {
            LocalDate currentDate = date.plusMonths(month);

            Installment installment = new Installment();
            installment.setTransaction(transaction);
            installment.setDate(currentDate);
            installment.setAmount(BigDecimal.TEN);
            installment.setWasPaid(Boolean.FALSE);
            installment.setInstallmentNumber(month + 1);

            installmentRepository.save(installment);
        }
    }


    @Test
    @DisplayName("Should find by transaction and date between when full period")
    void shouldFindByTransactionAndDateBetweenWhenFullPeriod() {
        List<Installment> installmentList = installmentRepository.findAllByTransactionAndDateBetween(
                transaction,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31)
        );

        Assertions.assertThat(installmentList)
                .hasSize(12);
    }

    @Test
    @DisplayName("Should find by transaction and date between")
    void shouldFindByTransactionAndDateBetween() {
        List<Installment> installmentList = installmentRepository.findAllByTransactionAndDateBetween(
                transaction,
                LocalDate.of(2025, 7, 1),
                LocalDate.of(2025, 12, 31)
        );

        Assertions.assertThat(installmentList)
                .hasSize(6);
    }
}
