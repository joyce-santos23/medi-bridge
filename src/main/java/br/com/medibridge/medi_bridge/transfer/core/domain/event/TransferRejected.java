package br.com.medibridge.medi_bridge.transfer.core.domain.event;

import java.time.Instant;
import java.util.UUID;

public record TransferRejected(
        UUID transferId,
        String reason,
        Instant occurredOn
) implements DomainEvent {
    public TransferRejected(UUID transferId, String reason) {
        this(transferId, reason, Instant.now());
    }
}
