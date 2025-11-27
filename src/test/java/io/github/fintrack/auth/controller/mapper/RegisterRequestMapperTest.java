package io.github.fintrack.auth.controller.mapper;

import io.github.fintrack.auth.controller.dto.RegisterRequest;
import io.github.fintrack.auth.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

public class RegisterRequestMapperTest {

    private final RegisterRequestMapper mapper = Mappers.getMapper(RegisterRequestMapper.class);

    @Test
    @DisplayName("Should map RegisterRequest to User")
    void shouldMapRegisterRequestToUser(){

        String name = "name test";
        String email = "email test";
        String pass = "password test";

        RegisterRequest request = new RegisterRequest(name, email, pass);

        User entity = mapper.toEntity(request);

        assertThat(entity.getName()).isEqualTo(name);
        assertThat(entity.getEmail()).isEqualTo(email);
        assertThat(entity.getPassword()).isEqualTo(pass);
        assertThat(entity.getUsername()).isEqualTo(email);
        assertThat(entity.isAccountNonExpired()).isTrue();
        assertThat(entity.isAccountNonLocked()).isTrue();
        assertThat(entity.isCredentialsNonExpired()).isTrue();
        assertThat(entity.isEnabled()).isTrue();

        assertThat(entity.getId()).isNull();
        assertThat(entity.getRole()).isNull();
    }

}
