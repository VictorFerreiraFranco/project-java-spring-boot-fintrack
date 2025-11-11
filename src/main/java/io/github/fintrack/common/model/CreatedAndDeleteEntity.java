package io.github.fintrack.common.model;

import io.github.fintrack.common.model.auditable.AuditableDeletion;
import jakarta.persistence.Embedded;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PostLoad;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class CreatedAndDeleteEntity extends CreatedEntity {
    @Embedded
    protected AuditableDeletion deletion = new AuditableDeletion();

    @PostLoad
    public void initEmbeddables() {
        if (this.deletion == null) {
            this.deletion = new AuditableDeletion();
        }
    }
}