package io.github.fintrack.auth.validator;

import io.github.fintrack._common.exception.DuplicateRecordException;
import io.github.fintrack.auth.model.User;
import io.github.fintrack.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;

    public void validate(User user) {
        if (userExists(user))
            throw new DuplicateRecordException("User already exists");
    }

    public boolean userExists(User user) {
        Optional<User> userFund = userRepository.findByEmail(user.getEmail());

        if (user.getId() == null)
            return userFund.isPresent();

        return userFund.isPresent() && !userFund.get().getId().equals(user.getId());
    }

}
