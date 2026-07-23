package br.com.medibridge.medi_bridge.notification.core.application.port;

import br.com.medibridge.medi_bridge.notification.core.application.dto.integration.NotificationOfferDetails;

import java.util.Optional;
import java.util.UUID;

public interface NotificationOfferGateway {

    Optional<NotificationOfferDetails> findOfferById(UUID id);
}
