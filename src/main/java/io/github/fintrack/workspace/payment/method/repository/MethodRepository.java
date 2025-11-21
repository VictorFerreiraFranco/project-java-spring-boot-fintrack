package io.github.fintrack.workspace.payment.method.repository;

import io.github.fintrack.workspace.payment.method.model.Method;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface MethodRepository extends JpaRepository<Method, UUID>, JpaSpecificationExecutor<Method> {
    Optional<Method> findByIdAndDeletion_DeletedAtIsNull(UUID id);
}
