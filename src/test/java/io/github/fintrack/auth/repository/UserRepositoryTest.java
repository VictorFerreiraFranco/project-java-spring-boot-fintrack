package io.github.fintrack.auth.repository;

import io.github.fintrack.auth.model.Role;
import io.github.fintrack.auth.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Should save and retrieve user by email")
    void shouldFindUserByEmail() {
        String email = "test@email.com";

        User user = User.builder()
                .name("Test Name")
                .email(email)
                .password("123")
                .role(Role.USER)
                .build();

        userRepository.save(user);

        Optional<User> result = userRepository.findByEmail(email);

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo(email);
    }

    @Test
    void shouldReturnEmptyWhenEmailNotExists() {
        Optional<User> result = userRepository.findByEmail("text@email.com.not.exist");

        assertThat(result).isNotPresent();
    }

}
