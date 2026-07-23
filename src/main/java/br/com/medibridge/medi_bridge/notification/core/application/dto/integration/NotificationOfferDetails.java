package br.com.medibridge.medi_bridge.notification.core.application.dto.integration;

import java.util.UUID;

public record NotificationOfferDetails(
        UUID id,
        String medicineName,
        Integer quantity,
        String unit,
        UUID hospitalId
) {}
