package br.com.medibridge.medi_bridge.offer.core.domain.offer.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record OfferCreated(
        UUID offerId,
        UUID hospitalId,
        LocalDateTime occurredOn
) implements DomainEvent {
    public OfferCreated(UUID offerId, UUID hospitalId) {
        this(offerId, hospitalId, LocalDateTime.now());
    }
}
