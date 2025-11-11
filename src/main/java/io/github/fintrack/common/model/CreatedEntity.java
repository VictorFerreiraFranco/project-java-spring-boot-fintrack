package io.github.fintrack.common.model;

import io.github.fintrack.common.model.auditable.AuditableCreation;
import jakarta.persistence.Embedded;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class CreatedEntity extends BaseEntity {
    @Embedded
    protected AuditableCreation creation = new AuditableCreation();
}
