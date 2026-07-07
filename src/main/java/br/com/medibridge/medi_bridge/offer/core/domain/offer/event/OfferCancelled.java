package br.com.medibridge.medi_bridge.offer.core.domain.offer.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record OfferCancelled(
        UUID offerId,
        LocalDateTime occurredOn
) implements DomainEvent {
    public OfferCancelled(UUID offerId) {
        this(offerId, LocalDateTime.now());
    }
}
