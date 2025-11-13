package io.github.fintrack.workspace.payment.method.controller.dto;

import lombok.Getter;

public record MethodFilter(
    String description,
    String type
) { }
