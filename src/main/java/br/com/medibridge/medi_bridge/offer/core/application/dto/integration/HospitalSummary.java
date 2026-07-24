package br.com.medibridge.medi_bridge.offer.core.application.dto.integration;

import java.util.UUID;

public record HospitalSummary(
        UUID id,
        String name,
        boolean active,
        String email,
        String phone,
        AddressSummary address
) {}
