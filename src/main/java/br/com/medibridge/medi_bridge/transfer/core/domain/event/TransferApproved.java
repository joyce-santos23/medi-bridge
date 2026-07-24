package br.com.medibridge.medi_bridge.transfer.core.domain.event;

import java.time.Instant;
import java.util.UUID;

public record TransferApproved(
        UUID transferId,
        Instant occurredOn
) implements DomainEvent {
    public TransferApproved(UUID transferId) {
        this(transferId, Instant.now());
    }
}
