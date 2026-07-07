package br.com.medibridge.medi_bridge.offer.core.domain.offer.event;

import java.time.LocalDateTime;

public interface DomainEvent {
    LocalDateTime occurredOn();
}
