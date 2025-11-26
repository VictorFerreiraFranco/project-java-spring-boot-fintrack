package io.github.fintrack.auth.repository;

import io.github.fintrack.auth.model.RefreshToken;
import io.github.fintrack.auth.model.Role;
import io.github.fintrack.auth.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;


    private final String token = "sample-token";
    private final String email = "test@email.com";

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .name("Test Name")
                .email(this.email)
                .password("123")
                .role(Role.USER)
                .build();

        userRepository.save(user);

        RefreshToken refreshToken = RefreshToken.builder()
                .token(this.token)
                .expiryDate(Instant.now().plusSeconds(10000))
                .user(user)
                .build();

        refreshTokenRepository.save(refreshToken);
    }

    @Test
    @DisplayName("Should find by token")
    void shouldFindByToken() {
        Optional<RefreshToken> result = refreshTokenRepository.findByToken(this.token);

        assertThat(result).isPresent();
        assertThat(result.get().getToken()).isEqualTo(this.token);
    }

    @Test
    @DisplayName("Should delete by user")
    void shouldDeleteByUser() {
        Optional<User> userOptional = userRepository.findByEmail(this.email);
        assertThat(userOptional).isPresent();

        refreshTokenRepository.deleteByUser(userOptional.get());

        assertThat(refreshTokenRepository.findByToken(this.token)).isEmpty();
        assertThat(userRepository.findByEmail(email)).isPresent();
    }
}
