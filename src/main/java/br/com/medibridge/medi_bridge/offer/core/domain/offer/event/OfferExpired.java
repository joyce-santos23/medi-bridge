package br.com.medibridge.medi_bridge.offer.core.domain.offer.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record OfferExpired(
        UUID offerId,
        LocalDateTime occurredOn
) implements DomainEvent {
    public OfferExpired(UUID offerId) {
        this(offerId, LocalDateTime.now());
    }
}
