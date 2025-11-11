package io.github.fintrack.common.model.auditable;

import io.github.fintrack.auth.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Embeddable
public class AuditableDeletion {

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deleted_by")
    private User deletedBy;

    public void markAsDeleted(User user) {
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = user;
    }

    public boolean isDeleted() {
        return this.deletedAt != null;
    }
}
