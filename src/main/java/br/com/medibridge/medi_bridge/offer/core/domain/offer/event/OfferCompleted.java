package br.com.medibridge.medi_bridge.offer.core.domain.offer.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record OfferCompleted(
        UUID offerId,
        LocalDateTime occurredOn
) implements DomainEvent {
    public OfferCompleted(UUID offerId) {
        this(offerId, LocalDateTime.now());
    }
}
