package br.com.medibridge.medi_bridge.offer.core.domain.offer.event;

import java.time.Instant;
import java.util.UUID;

public record OfferCreated(
        UUID offerId,
        UUID hospitalId,
        Instant occurredOn
) implements DomainEvent {
    public OfferCreated(UUID offerId, UUID hospitalId) {
        this(offerId, hospitalId, Instant.now());
    }
}
