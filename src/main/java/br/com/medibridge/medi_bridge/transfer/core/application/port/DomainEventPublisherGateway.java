package br.com.medibridge.medi_bridge.transfer.core.application.port;

import br.com.medibridge.medi_bridge.transfer.core.domain.event.DomainEvent;
import java.util.Collection;

public interface DomainEventPublisherGateway {
    void publish(Collection<DomainEvent> events);
}
