package br.com.medibridge.medi_bridge.notification.core.application.dto.integration;

import java.util.UUID;

public record NotificationHospitalSummary(
        UUID id,
        String name,
        String email
) {}
