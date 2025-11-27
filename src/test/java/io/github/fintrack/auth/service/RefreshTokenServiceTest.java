package io.github.fintrack.auth.service;

import io.github.fintrack.auth.exception.TokenRefreshException;
import io.github.fintrack.auth.model.RefreshToken;
import io.github.fintrack.auth.model.User;
import io.github.fintrack.auth.repository.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

public class RefreshTokenServiceTest {

    private RefreshTokenRepository refreshTokenRepository;
    private RefreshTokenService refreshTokenService;


    @BeforeEach
    void setUp() {
        refreshTokenRepository = mock(RefreshTokenRepository.class);
        refreshTokenService = new RefreshTokenService(refreshTokenRepository);

        try {

            Field field = RefreshTokenService.class.getDeclaredField("refreshTokenDurationMs");
            field.setAccessible(true);
            field.set(refreshTokenService, 10000L);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    @DisplayName("Should find by token")
    void sheduleFindByToken(){
        String token = "sample-token";
        RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .build();

        when(refreshTokenRepository.findByToken(token))
                .thenReturn(Optional.of(refreshToken));

        Optional<RefreshToken> result = refreshTokenService.findByToken(token);

        assertThat(result).isPresent();
        assertThat(result.get().getToken()).isEqualTo(token);

        verify(refreshTokenRepository).findByToken(token);
    }

    @Test
    @DisplayName("Should save token")
    void sheduleSave(){
        RefreshToken refreshToken = new RefreshToken();

        when(refreshTokenRepository.save(refreshToken))
                .thenReturn(refreshToken);

        refreshTokenService.save(refreshToken);

        verify(refreshTokenRepository).save(refreshToken);
    }

    @Test
    @DisplayName("Should delete token")
    void sheduleDelete(){
        RefreshToken refreshToken = new RefreshToken();

        refreshTokenService.delete(refreshToken);

        verify(refreshTokenRepository).delete(refreshToken);
    }

    @Test
    @DisplayName("Should delete by user")
    void shouldDeleteByUser() {
        User user = User.builder()
                .name("Test User")
                .build();

        refreshTokenService.deleteByUser(user);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(refreshTokenRepository).deleteByUser(captor.capture());

        assertThat(captor.getValue()).isEqualTo(user);
    }

    @Test
    @DisplayName("Should create token for user and delete old tokens")
    void shouldCreateByUser() {
        User user = new User();

        ArgumentCaptor<RefreshToken> captor = ArgumentCaptor.forClass(RefreshToken.class);

        when(refreshTokenRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        RefreshToken token = refreshTokenService.createByUser(user);

        verify(refreshTokenRepository).deleteByUser(user);
        verify(refreshTokenRepository).save(captor.capture());

        RefreshToken saved = captor.getValue();

        assertThat(saved.getUser()).isEqualTo(user);
        assertThat(saved.getExpiryDate()).isAfter(Instant.now());
        assertThat(saved.getToken()).isNotNull();

        assertThat(token).isEqualTo(saved);
    }

    @Test
    @DisplayName("Should throw exception if token expired")
    void shouldThrowIfExpired() {
        RefreshToken token = RefreshToken.builder()
                .token("abc")
                .expiryDate(Instant.now().minusSeconds(10))
                .build();

        assertThatThrownBy(() -> refreshTokenService.verifyExpiration(token))
                .isInstanceOf(TokenRefreshException.class)
                .hasMessageContaining("Refresh token was expired");

        verify(refreshTokenRepository).delete(token);
    }

    @Test
    @DisplayName("Should return token if not expired")
    void shouldReturnTokenIfValid() {
        RefreshToken token = RefreshToken.builder()
                .token("abc")
                .expiryDate(Instant.now().plusSeconds(10))
                .build();

        RefreshToken result = refreshTokenService.verifyExpiration(token);

        assertThat(result).isEqualTo(token);
        verify(refreshTokenRepository, never()).delete(token);
    }
}
