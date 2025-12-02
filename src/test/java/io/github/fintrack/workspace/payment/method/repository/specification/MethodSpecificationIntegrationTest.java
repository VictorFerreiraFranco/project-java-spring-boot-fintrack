package io.github.fintrack.workspace.payment.method.repository.specification;

import io.github.fintrack.auth.model.Role;
import io.github.fintrack.auth.model.User;
import io.github.fintrack.auth.repository.UserRepository;
import io.github.fintrack.workspace.member.model.Member;
import io.github.fintrack.workspace.member.repository.MemberRepository;
import io.github.fintrack.workspace.payment.method.controller.dto.MethodFilter;
import io.github.fintrack.workspace.payment.method.model.Method;
import io.github.fintrack.workspace.payment.method.model.Type;
import io.github.fintrack.workspace.payment.method.repository.MethodRepository;
import io.github.fintrack.workspace.workspace.model.Workspace;
import io.github.fintrack.workspace.workspace.repository.WorkspaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class MethodSpecificationIntegrationTest {

    @Autowired
    private MethodRepository methodRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Autowired
    private MemberRepository memberRepository;

    private User user;
    private Workspace workspace;
    private Method method1;
    private Method method2;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("Test User")
                .email("test@gmail.com")
                .password("123")
                .role(Role.USER)
                .build();

        userRepository.save(user);

        workspace = Workspace.builder()
                .name("WS Test")
                .type(io.github.fintrack.workspace.workspace.model.Type.MAIN)
                .build();

        workspace.getCreation().setCreatedBy(user);
        workspace.getCreation().setCreatedAt(LocalDateTime.now());
        workspaceRepository.save(workspace);

        Member member = Member.builder()
                .user(user)
                .workspace(workspace)
                .role(io.github.fintrack.workspace.member.model.Role.OWNER)
                .build();

        member.getCreation().setCreatedBy(user);
        member.getCreation().setCreatedAt(LocalDateTime.now());
        memberRepository.save(member);

        method1 = new Method();
        method1.setDescription("Debit Card");
        method1.setType(Type.DEBIT_CARD);
        method1.setWorkspace(workspace);
        method1.getCreation().setCreatedBy(user);
        method1.getCreation().setCreatedAt(LocalDateTime.now());

        method2 = new Method();
        method2.setDescription("Bank Account");
        method2.setType(Type.BANK_TRANSFER);
        method2.setWorkspace(workspace);
        method2.getCreation().setCreatedBy(user);
        method2.getCreation().setCreatedAt(LocalDateTime.now());

        methodRepository.saveAll(List.of(method1, method2));
    }

    @Test
    @DisplayName("descriptionLike should filter by description")
    void descriptionLikeShouldFilter() {
        var spec = MethodSpecification.descriptionLike("debit");

        var result = methodRepository.findAll(spec);

        assertThat(result)
                .hasSize(1)
                .contains(method1)
                .doesNotContain(method2);
    }

    @Test
    @DisplayName("typeEqual should filter by type")
    void typeEqualShouldFilterByType() {
        var spec = MethodSpecification.typeEqual(Type.BANK_TRANSFER);

        var result = methodRepository.findAll(spec);

        assertThat(result)
                .hasSize(1)
                .contains(method2)
                .doesNotContain(method1);
    }

    @Test
    @DisplayName("workspaceEqual should filter methods by workspace")
    void workspaceEqualShouldFilterByWorkspace() {
        // Outro workspace
        Workspace otherWs = Workspace.builder()
                .name("Other")
                .type(io.github.fintrack.workspace.workspace.model.Type.MAIN)
                .build();

        otherWs.getCreation().setCreatedBy(user);
        otherWs.getCreation().setCreatedAt(LocalDateTime.now());
        workspaceRepository.save(otherWs);

        Method otherMethod = new Method();
        otherMethod.setDescription("Pix");
        otherMethod.setType(Type.PIX);
        otherMethod.setWorkspace(otherWs);
        otherMethod.getCreation().setCreatedBy(user);
        otherMethod.getCreation().setCreatedAt(LocalDateTime.now());

        methodRepository.save(otherMethod);

        var spec = MethodSpecification.workspaceEqual(workspace);

        var result = methodRepository.findAll(spec);

        assertThat(result)
                .hasSize(2)
                .contains(method1, method2)
                .doesNotContain(otherMethod);
    }

    @Test
    @DisplayName("deletedAtIsNull should return only active methods")
    void deletedAtIsNullShouldReturnOnlyActiveMethods() {
        method2.getDeletion().markAsDeleted(user);
        methodRepository.save(method2);

        var spec = MethodSpecification.deletedAtIsNull();

        var result = methodRepository.findAll(spec);

        assertThat(result)
                .hasSize(1)
                .contains(method1)
                .doesNotContain(method2);
    }

    @Test
    @DisplayName("applyFilter should combine description and type")
    void applyFilterShouldCombineFilters() {
        MethodFilter filter = new MethodFilter("BANK_TRANSFER", "Bank Account");

        var spec = MethodSpecification.applyFilter(filter);

        var result = methodRepository.findAll(spec);

        assertThat(result)
                .hasSize(1)
                .contains(method2)
                .doesNotContain(method1);
    }

    @Test
    @DisplayName("applyFilter should return all when filter is null")
    void applyFilterShouldReturnAllWhenNull() {
        var result = methodRepository.findAll(MethodSpecification.applyFilter(null));

        assertThat(result)
                .hasSize(2)
                .contains(method1, method2);
    }

    @Test
    @DisplayName("applyFilter should ignore empty fields")
    void applyFilterShouldIgnoreEmptyFields() {
        MethodFilter filter = new MethodFilter("", "");

        var result = methodRepository.findAll(MethodSpecification.applyFilter(filter));

        assertThat(result)
                .hasSize(2)
                .contains(method1, method2);
    }
}
