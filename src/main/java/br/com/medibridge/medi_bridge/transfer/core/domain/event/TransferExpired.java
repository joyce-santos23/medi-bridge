package br.com.medibridge.medi_bridge.transfer.core.domain.event;

import java.time.Instant;
import java.util.UUID;

public record TransferExpired(
        UUID transferId,
        Instant occurredOn
) implements DomainEvent {
    public TransferExpired(UUID transferId) {
        this(transferId, Instant.now());
    }
}
