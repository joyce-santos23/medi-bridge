package br.com.medibridge.medi_bridge.transfer.core.application.dto.integration;

import java.time.LocalDate;

public record ProductSummary(
        String name,
        String category,
        String manufacturer,
        String batch,
        LocalDate expirationDate,
        Integer quantity,
        String unit,
        String observations
) {}
