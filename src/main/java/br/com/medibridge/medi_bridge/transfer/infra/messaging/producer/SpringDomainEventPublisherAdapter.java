package br.com.medibridge.medi_bridge.transfer.infra.messaging.producer;

import br.com.medibridge.medi_bridge.transfer.core.application.port.DomainEventPublisherGateway;
import br.com.medibridge.medi_bridge.transfer.core.domain.event.DomainEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@Slf4j
public class SpringDomainEventPublisherAdapter implements DomainEventPublisherGateway {

    private final ApplicationEventPublisher applicationEventPublisher;

    public SpringDomainEventPublisherAdapter(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publish(Collection<DomainEvent> events) {
        if (events == null || events.isEmpty()) {
            return;
        }
        log.info("Publishing {} transfer domain events", events.size());
        for (DomainEvent event : events) {
            applicationEventPublisher.publishEvent(event);
        }
    }
}
