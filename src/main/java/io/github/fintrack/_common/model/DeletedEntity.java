package io.github.fintrack._common.model;

import io.github.fintrack._common.model.auditable.AuditableDeletion;
import jakarta.persistence.Embedded;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class DeletedEntity extends BaseEntity {
    @Embedded
    protected AuditableDeletion creation = new AuditableDeletion();
}
