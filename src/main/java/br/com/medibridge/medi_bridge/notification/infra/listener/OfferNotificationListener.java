package br.com.medibridge.medi_bridge.notification.infra.listener;

import br.com.medibridge.medi_bridge.offer.core.domain.offer.event.OfferAvailable;
import br.com.medibridge.medi_bridge.offer.core.domain.offer.event.OfferCreated;
import br.com.medibridge.medi_bridge.notification.core.application.usecase.SendOfferAvailableNotificationUseCase;
import br.com.medibridge.medi_bridge.notification.core.application.usecase.SendOfferCreatedNotificationUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OfferNotificationListener {

    private final SendOfferCreatedNotificationUseCase sendOfferCreatedNotificationUseCase;
    private final SendOfferAvailableNotificationUseCase sendOfferAvailableNotificationUseCase;

    @EventListener
    @Async
    public void onOfferCreated(OfferCreated event) {
        log.info("Received OfferCreated event for offer ID: {}", event.offerId());
        try {
            sendOfferCreatedNotificationUseCase.execute(event.offerId(), event.hospitalId());
        } catch (Exception e) {
            log.error("Error processing OfferCreated notification for offer ID: {}", event.offerId(), e);
        }
    }

    @EventListener
    @Async
    public void onOfferAvailable(OfferAvailable event) {
        log.info("Received OfferAvailable event for offer ID: {}", event.offerId());
        try {
            sendOfferAvailableNotificationUseCase.execute(event.offerId());
        } catch (Exception e) {
            log.error("Error processing OfferAvailable notification for offer ID: {}", event.offerId(), e);
        }
    }
}
