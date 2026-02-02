package io.github.fintrack.workspace.member.service;

import io.github.fintrack.auth.model.User;
import io.github.fintrack.auth.service.AuthService;
import io.github.fintrack.workspace.invite.model.Invite;
import io.github.fintrack.workspace.member.exception.MemberNotFoundException;
import io.github.fintrack.workspace.member.model.Member;
import io.github.fintrack.workspace.member.repository.MemberRepository;
import io.github.fintrack.workspace.member.service.validator.MemberValidator;
import io.github.fintrack.workspace.workspace.model.Workspace;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemberServiceTest {

    private final MemberRepository memberRepository = mock(MemberRepository.class);
    private final MemberValidator memberValidator = mock(MemberValidator.class);
    private final AuthService authService = mock(AuthService.class);

    private final MemberService memberService = new MemberService(
            memberRepository, memberValidator, authService
    );

    @Test
    @DisplayName("Should find by id and deletedAt is null")
    void shouldFindByIdAndDeletedAtIsNull() {
        UUID id = UUID.randomUUID();
        Member member = new Member();
        member.setId(id);

        when(memberRepository.findByIdAndDeletionDeletedAtIsNull(id))
                .thenReturn(Optional.of(member));

        Optional<Member> result = memberService.findByIdAndDeletedAtIsNull(id);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(id);

        verify(memberRepository).findByIdAndDeletionDeletedAtIsNull(id);
    }

    @Test
    @DisplayName("Should find all by workspace and deletedAt is null")
    void shouldFindAllByWorkspaceAndDeletedAtIsNull() {
        Workspace workspace = new Workspace();
        List<Member> expectedMembers = List.of(new Member(), new Member());

        when(memberRepository.findAll(any(Specification.class)))
                .thenReturn(expectedMembers);

        List<Member> result = memberService.findAllByWorkspaceAndDeletedAtIsNull(workspace);

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Should return member when exists")
    void shouldReturnMemberWhenExists() {
        UUID id = UUID.randomUUID();
        Member member = new Member();

        when(memberRepository.findByIdAndDeletionDeletedAtIsNull(id))
                .thenReturn(Optional.of(member));

        Member result = memberService.findByIdAndValidateExistence(id);

        assertThat(result).isEqualTo(member);
    }

    @Test
    @DisplayName("Should throw exception when member does not exist")
    void shouldThrowExceptionWhenMemberDoesNotExist() {
        UUID id = UUID.randomUUID();

        when(memberRepository.findByIdAndDeletionDeletedAtIsNull(id))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                memberService.findByIdAndValidateExistence(id)
        ).isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("Should save member")
    void shouldSaveMember() {
        Member member = new Member();

        memberService.save(member);

        verify(memberValidator).validToSave(member);
        verify(memberRepository).save(member);
    }

    @Test
    @DisplayName("Should delete member")
    void shouldDeleteMember() {
        User loggedUser = new User();
        Member member = new Member();

        when(authService.getUserLoggedIn()).thenReturn(loggedUser);
        when(memberRepository.save(member)).thenReturn(member);

        memberService.delete(member);

        verify(memberValidator).validToDelete(member);

        assertThat(member.getDeletion().getDeletedAt()).isNotNull();
        assertThat(member.getDeletion().getDeletedBy()).isEqualTo(loggedUser);

        verify(memberRepository).save(member);
    }

    @Test
    @DisplayName("Should create owner member")
    void shouldCreateOwnerMember() {
        User user = new User();
        Workspace workspace = new Workspace();

        doAnswer(inv -> inv.<Member>getArgument(0))
                .when(memberRepository).save(any());

        memberService.createOwner(user, workspace);

        verify(memberValidator).validToSave(any(Member.class));
        verify(memberRepository).save(any(Member.class));

        Member captured = new Member();
        captured.setUser(user);
        captured.setWorkspace(workspace);
    }

    @Test
    @DisplayName("Should create member by invite")
    void shouldCreateMemberByInvite() {
        User to = new User();
        Workspace workspace = new Workspace();

        Invite invite = new Invite();
        invite.setTo(to);
        invite.setWorkspace(workspace);

        doAnswer(inv -> inv.getArgument(0)).when(memberRepository).save(any());

        memberService.createByInvite(invite);

        verify(memberValidator).validToSave(any(Member.class));
        verify(memberRepository).save(any(Member.class));
    }
}
