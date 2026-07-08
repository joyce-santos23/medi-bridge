package br.com.medibridge.medi_bridge.offer.core.domain.offer.event;

import java.time.Instant;

public interface DomainEvent {
    Instant occurredOn();
}
