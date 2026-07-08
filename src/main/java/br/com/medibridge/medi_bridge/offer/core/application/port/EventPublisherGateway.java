package br.com.medibridge.medi_bridge.offer.core.application.port;

import br.com.medibridge.medi_bridge.offer.core.domain.offer.event.DomainEvent;
import java.util.Collection;

public interface EventPublisherGateway {
    void publish(DomainEvent event);
    void publish(Collection<DomainEvent> events);
}
