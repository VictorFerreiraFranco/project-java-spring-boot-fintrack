package io.github.fintrack.auth.controller.mapper;

import io.github.fintrack.auth.controller.dto.RegisterRequest;
import io.github.fintrack.auth.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RegisterRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    User toEntity(RegisterRequest registerRequest);
}
