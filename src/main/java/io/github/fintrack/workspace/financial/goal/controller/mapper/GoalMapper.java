package io.github.fintrack.workspace.financial.goal.controller.mapper;

import io.github.fintrack.workspace.financial.category.controller.mapper.CategoryMapper;
import io.github.fintrack.workspace.financial.goal.controller.dto.GoalRequest;
import io.github.fintrack.workspace.financial.goal.controller.dto.GoalResponse;
import io.github.fintrack.workspace.financial.goal.model.Goal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = "spring",
        uses = {CategoryMapper.class}
)
public interface GoalMapper {

    @Mapping(target = "created", expression = "java( goal.getCreation().getCreatedAt() )")
    GoalResponse toResponse(Goal goal);

    @Mapping(target = "category", ignore = true)
    Goal toEntity(GoalRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "creation", ignore = true)
    @Mapping(target = "deletion", ignore = true)
    void updateEntity(@MappingTarget Goal entity, GoalRequest request);
}
