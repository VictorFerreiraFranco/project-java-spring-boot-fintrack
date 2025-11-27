package io.github.fintrack.auth.controller.mapper;

import io.github.fintrack.auth.controller.dto.UserResponse;
import io.github.fintrack.auth.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserResponseMapper {
    UserResponse toDto(User user);
}
