package br.com.medibridge.medi_bridge.offer.core.application.dto.integration;

import java.util.UUID;

public record UserSummary(
        UUID id,
        String name,
        String professionalCouncil,
        String professionalRegistration
) {}
