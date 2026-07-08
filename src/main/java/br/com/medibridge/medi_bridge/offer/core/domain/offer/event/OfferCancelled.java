package br.com.medibridge.medi_bridge.offer.core.domain.offer.event;

import java.time.Instant;
import java.util.UUID;

public record OfferCancelled(
        UUID offerId,
        Instant occurredOn
) implements DomainEvent {
    public OfferCancelled(UUID offerId) {
        this(offerId, Instant.now());
    }
}
