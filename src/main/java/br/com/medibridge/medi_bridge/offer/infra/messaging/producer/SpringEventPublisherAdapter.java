package br.com.medibridge.medi_bridge.offer.infra.messaging.producer;

import br.com.medibridge.medi_bridge.offer.core.application.port.EventPublisherGateway;
import br.com.medibridge.medi_bridge.offer.core.domain.offer.event.DomainEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@Slf4j
public class SpringEventPublisherAdapter implements EventPublisherGateway {

    private final ApplicationEventPublisher applicationEventPublisher;

    public SpringEventPublisherAdapter(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publish(Collection<DomainEvent> events) {
        if (events == null || events.isEmpty()) {
            return;
        }
        log.info("Publishing {} domain events", events.size());
        for (DomainEvent event : events) {
            applicationEventPublisher.publishEvent(event);
        }
    }
}
