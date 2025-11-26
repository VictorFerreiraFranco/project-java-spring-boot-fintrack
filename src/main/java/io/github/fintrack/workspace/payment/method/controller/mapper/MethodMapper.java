package io.github.fintrack.workspace.payment.method.controller.mapper;

import io.github.fintrack.workspace.payment.method.controller.dto.MethodRequest;
import io.github.fintrack.workspace.payment.method.controller.dto.MethodResponse;
import io.github.fintrack.workspace.payment.method.model.Method;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MethodMapper {

    @Mapping(target = "created", expression = "java( method.getCreation().getCreatedAt() )")
    MethodResponse toResponse(Method method);

    @Mapping(target = "workspace", ignore = true)
    Method toEntity(MethodRequest methodRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "workspace", ignore = true)
    @Mapping(target = "creation", ignore = true)
    @Mapping(target = "deletion", ignore = true)
    void updateEntity(@MappingTarget Method entity, MethodRequest request);
}
