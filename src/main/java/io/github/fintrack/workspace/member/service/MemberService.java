package io.github.fintrack.workspace.member.service;

import io.github.fintrack.auth.model.User;
import io.github.fintrack.auth.service.AuthService;
import io.github.fintrack.workspace.invite.model.Invite;
import io.github.fintrack.workspace.member.model.Member;
import io.github.fintrack.workspace.member.model.Role;
import io.github.fintrack.workspace.member.repository.MemberRepository;
import io.github.fintrack.workspace.member.service.validator.MemberValidator;
import io.github.fintrack.workspace.workspace.model.Workspace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberValidator memberValidator;
    private final AuthService authService;

    public Optional<Member> findByIdAndDeletedAtIsNull(UUID id) {
        return memberRepository.findByIdAndDeletion_DeletedAtIsNull(id);
    }

    public List<Member> findAllByWorkspaceAndDeletedAtIsNull(Workspace workspace) {
        return memberRepository.findAllByWorkspaceAndDeletion_DeletedAtIsNull(workspace);
    }

    @Transactional
    public void save(Member member){
        memberValidator.validToSave(member);
        memberRepository.save(member);
    }

    @Transactional
    public void delete(Member member){
        memberValidator.validToDelete(member);
        member.getDeletion().markAsDeleted(authService.getUserLoggedIn());
        memberRepository.save(member);
    }

    public void createOwner(User user, Workspace workspace) {
        Member member = Member.builder()
                .user(user)
                .workspace(workspace)
                .role(Role.OWNER)
                .createdBy(user)
                .build();
        this.save(member);
    }

    public void createByInvite(Invite invite) {
        Member member = Member.builder()
                .user(invite.getTo())
                .workspace(invite.getWorkspace())
                .role(Role.MEMBER)
                .build();
        this.save(member);
    }
}
