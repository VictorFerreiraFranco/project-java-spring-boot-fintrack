package io.github.fintrack.auth.controller.mapper;

import io.github.fintrack.auth.controller.dto.UserResponse;
import io.github.fintrack.auth.model.Role;
import io.github.fintrack.auth.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class UserResponseMapperTest {

    private final UserResponseMapper mapper = Mappers.getMapper(UserResponseMapper.class);

    @Test
    @DisplayName("Should map User to UserResponse")
    void shouldMapUserToUserResponse(){

        UUID id = UUID.randomUUID();
        String name = "name test";
        String email = "email test";
        Role role = Role.USER;

        User user = User.builder()
                .id(id)
                .name(name)
                .email(email)
                .role(role)
                .build();

        UserResponse dto = mapper.toDto(user);

        assertThat(dto.id()).isEqualTo(id);
        assertThat(dto.name()).isEqualTo(name);
        assertThat(dto.email()).isEqualTo(email);
        assertThat(dto.role()).isEqualTo(role);
    }

}
