package io.github.fintrack.workspace.member.service;

import io.github.fintrack.auth.model.User;
import io.github.fintrack.workspace.invite.model.Invite;
import io.github.fintrack.workspace.member.model.Member;
import io.github.fintrack.workspace.member.model.Role;
import io.github.fintrack.workspace.member.repository.MemberRepository;
import io.github.fintrack.workspace.member.validator.MemberValidator;
import io.github.fintrack.workspace.workspace.model.Workspace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberValidator memberValidator;

    public void save(Member member){
        memberValidator.validToSave(member);
        memberRepository.save(member);
    }

    public void delete(Member member){
        memberValidator.validToDelete(member);
        memberRepository.delete(member);
    }

    public Optional<Member> findByIdAndDeletedAtIsNull(UUID id) {
        return memberRepository.findByIdAndDeletion_DeletedAtIsNull(id);
    }

    public List<Member> findAllByWorkspaceAndDeletedAtIsNull(Workspace workspace) {
        return memberRepository.findAllByWorkspaceAndDeletion_DeletedAtIsNull(workspace);
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
