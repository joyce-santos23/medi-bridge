package br.com.medibridge.medi_bridge.notification.core.application.dto.integration;

import java.util.UUID;

public record NotificationTransferDetails(
        UUID id,
        UUID offerId,
        UUID sourceHospitalId,
        UUID destinationHospitalId,
        String reason,
        String confirmationCode
) {}
