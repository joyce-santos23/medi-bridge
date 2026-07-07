package br.com.medibridge.medi_bridge.offer.core.domain.offer.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record OfferAvailable(
        UUID offerId,
        LocalDateTime occurredOn
) implements DomainEvent {
    public OfferAvailable(UUID offerId) {
        this(offerId, LocalDateTime.now());
    }
}
