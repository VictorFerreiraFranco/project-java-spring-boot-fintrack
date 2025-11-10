package io.github.fintrack.auth.controller.mapper;

import io.github.fintrack.auth.controller.dto.UserRegisterRequest;
import io.github.fintrack.auth.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserRegisterRequest registerRequest);
}
