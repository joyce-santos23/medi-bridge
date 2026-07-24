package br.com.medibridge.medi_bridge.transfer.core.domain.event;

import java.time.Instant;

public interface DomainEvent {
    Instant occurredOn();
}
