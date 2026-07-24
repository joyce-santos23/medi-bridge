package br.com.medibridge.medi_bridge.notification.core.application.port;

import br.com.medibridge.medi_bridge.notification.core.application.dto.integration.NotificationHospitalSummary;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationCatalogGateway {

    Optional<NotificationHospitalSummary> findHospitalById(UUID id);
    List<NotificationHospitalSummary> findAllActiveHospitals();
}
