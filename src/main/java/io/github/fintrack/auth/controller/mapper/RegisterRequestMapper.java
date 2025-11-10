package io.github.fintrack.auth.controller.mapper;

import io.github.fintrack.auth.controller.dto.RegisterRequest;
import io.github.fintrack.auth.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RegisterRequestMapper {
    User toEntity(RegisterRequest registerRequest);
}
