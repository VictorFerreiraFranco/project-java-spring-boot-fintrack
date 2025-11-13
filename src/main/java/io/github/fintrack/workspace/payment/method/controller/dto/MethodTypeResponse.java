package io.github.fintrack.workspace.payment.method.controller.dto;

import io.github.fintrack.workspace.payment.method.model.Type;

public record MethodTypeResponse(
    Type type,
    String description
) { }
