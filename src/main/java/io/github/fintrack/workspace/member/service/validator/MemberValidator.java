package io.github.fintrack.workspace.member.service.validator;

import io.github.fintrack.common.exception.DuplicateRecordException;
import io.github.fintrack.auth.service.AuthService;
import io.github.fintrack.workspace.member.exception.CannotDeleteOwner;
import io.github.fintrack.workspace.member.exception.CannotPermissionDelete;
import io.github.fintrack.workspace.member.model.Member;
import io.github.fintrack.workspace.member.model.Role;
import io.github.fintrack.workspace.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MemberValidator {

    private final MemberRepository memberRepository;
    private final AuthService authService;

    public void validToSave(Member member) {
        if (memberExists(member))
            throw new DuplicateRecordException("Member already exists");
    }

    public void validToDelete(Member member) {
        if (member.isOwner())
            throw new CannotDeleteOwner();

        if (!userLoggedIsEqual(member) && !userLoggedInIsOwner(member))
            throw new CannotPermissionDelete();
    }

    public boolean memberExists(Member member) {
        Optional<Member> memberFund = memberRepository.findByWorkspaceAndUserAndDeletion_DeletedAtIsNull(member.getWorkspace(), member.getUser());

        if (member.getId() == null)
            return memberFund.isPresent();

        return memberFund.isPresent() && !memberFund.get().getId().equals(member.getId());
    }

    public boolean userLoggedInIsOwner(Member member) {
        List<Member> ownerList = memberRepository.findAllByWorkspaceAndRole(member.getWorkspace(), Role.OWNER);

        if (ownerList.isEmpty())
            return false;

        return userLoggedIsEqual(ownerList.getFirst());
    }

    public boolean userLoggedIsEqual(Member member) {
        return member.getUser().getId().equals(authService.getUserLoggedIn().getId());
    }
}
