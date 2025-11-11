package io.github.fintrack.common.model;

import io.github.fintrack.common.model.auditable.AuditableDeletion;
import jakarta.persistence.Embedded;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class DeletedEntity extends BaseEntity {
    @Embedded
    protected AuditableDeletion deletion = new AuditableDeletion();
}
