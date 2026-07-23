package br.com.medibridge.medi_bridge.transfer.core.application.dto.integration;

import java.util.UUID;

public record OfferSummary(
        UUID id,
        UUID hospitalId,
        ProductSummary product
) {}
