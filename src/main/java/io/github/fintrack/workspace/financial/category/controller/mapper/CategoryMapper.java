package io.github.fintrack.workspace.financial.category.controller.mapper;

import io.github.fintrack.workspace.financial.category.controller.dto.CategoryRequest;
import io.github.fintrack.workspace.financial.category.controller.dto.CategoryResponse;
import io.github.fintrack.workspace.financial.category.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(target = "created", expression = "java( category.getCreation().getCreatedAt() )")
    CategoryResponse toResponse(Category category);

    Category toEntity(CategoryRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "workspace", ignore = true)
    @Mapping(target = "creation", ignore = true)
    @Mapping(target = "deletion", ignore = true)
    void updateEntity(@MappingTarget Category entity, CategoryRequest request);
}
