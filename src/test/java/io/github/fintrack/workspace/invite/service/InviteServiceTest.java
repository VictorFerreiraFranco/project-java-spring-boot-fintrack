package io.github.fintrack.workspace.invite.service;

import io.github.fintrack.auth.model.User;
import io.github.fintrack.workspace.invite.exception.InviteNotFoundException;
import io.github.fintrack.workspace.invite.model.Invite;
import io.github.fintrack.workspace.invite.model.Status;
import io.github.fintrack.workspace.invite.repository.InviteRepository;
import io.github.fintrack.workspace.invite.service.validator.InviteValidator;
import io.github.fintrack.workspace.member.service.MemberService;
import io.github.fintrack.workspace.workspace.model.Workspace;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class InviteServiceTest {

    private final InviteRepository inviteRepository = mock(InviteRepository.class);
    private final InviteValidator inviteValidator = mock(InviteValidator.class);
    private final MemberService memberService = mock(MemberService.class);

    private final InviteService inviteService = new InviteService(
            inviteRepository, inviteValidator, memberService
    );

    @Test
    @DisplayName("Should find invite by id")
    void shouldFindById() {
        UUID id = UUID.randomUUID();
        Invite invite = new Invite();
        invite.setId(id);

        when(inviteRepository.findById(id)).thenReturn(Optional.of(invite));

        Optional<Invite> result = inviteService.findById(id);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(id);

        verify(inviteRepository).findById(id);
    }

    @Test
    @DisplayName("Should find all pending invites by workspace")
    void shouldFindAllPendingByWorkspace() {
        Workspace workspace = new Workspace();
        List<Invite> invites = List.of(new Invite(), new Invite());

        when(inviteRepository.findAll(any(Specification.class)))
                .thenReturn(invites);

        List<Invite> result = inviteService.findAllIsPendingByWorkspace(workspace);

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Should find all pending invites by user")
    void shouldFindAllPendingByUser() {
        User user = new User();
        List<Invite> invites = List.of(new Invite());

        when(inviteRepository.findAll(any(Specification.class)))
                .thenReturn(invites);

        List<Invite> result = inviteService.findAllIsPendingByUser(user);

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("Should return invite when exists")
    void shouldReturnInviteWhenExists() {
        UUID id = UUID.randomUUID();
        Invite invite = new Invite();

        when(inviteRepository.findById(id)).thenReturn(Optional.of(invite));

        Invite result = inviteService.findByIdAndValidateExistence(id);

        assertThat(result).isEqualTo(invite);
    }

    @Test
    @DisplayName("Should throw exception when invite does not exist")
    void shouldThrowExceptionWhenInviteDoesNotExist() {
        UUID id = UUID.randomUUID();

        when(inviteRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                inviteService.findByIdAndValidateExistence(id)
        ).isInstanceOf(InviteNotFoundException.class);
    }

    @Test
    @DisplayName("Should create invite with pending status")
    void shouldCreateInvite() {
        Invite invite = new Invite();

        when(inviteRepository.save(invite)).thenReturn(invite);

        Invite result = inviteService.create(invite);

        verify(inviteValidator).validToCreate(invite);
        assertThat(invite.getStatus()).isEqualTo(Status.PENDING);
        verify(inviteRepository).save(invite);

        assertThat(result).isEqualTo(invite);
    }

    @Test
    @DisplayName("Should accept invite and create member")
    void shouldAcceptInvite() {
        Invite invite = new Invite();

        when(inviteRepository.save(invite)).thenReturn(invite);

        inviteService.acceptInvite(invite);

        verify(inviteValidator).validToAccept(invite);
        assertThat(invite.getStatus()).isEqualTo(Status.ACCEPTED);

        verify(inviteRepository).save(invite);
        verify(memberService).createByInvite(invite);
    }

    @Test
    @DisplayName("Should refuse invite")
    void shouldRefuseInvite() {
        Invite invite = new Invite();

        when(inviteRepository.save(invite)).thenReturn(invite);

        inviteService.refuseInvite(invite);

        verify(inviteValidator).validToRefuse(invite);
        assertThat(invite.getStatus()).isEqualTo(Status.REFUSED);
        verify(inviteRepository).save(invite);
    }

    @Test
    @DisplayName("Should cancel invite")
    void shouldCancelInvite() {
        Invite invite = new Invite();

        when(inviteRepository.save(invite)).thenReturn(invite);

        inviteService.canceledInvite(invite);

        verify(inviteValidator).validToCanceled(invite);
        assertThat(invite.getStatus()).isEqualTo(Status.CANCELED);
        verify(inviteRepository).save(invite);
    }
}
