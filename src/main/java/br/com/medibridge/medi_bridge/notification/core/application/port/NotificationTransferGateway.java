package br.com.medibridge.medi_bridge.notification.core.application.port;

import br.com.medibridge.medi_bridge.notification.core.application.dto.integration.NotificationTransferDetails;

import java.util.Optional;
import java.util.UUID;

public interface NotificationTransferGateway {

    Optional<NotificationTransferDetails> findTransferById(UUID id);
}
