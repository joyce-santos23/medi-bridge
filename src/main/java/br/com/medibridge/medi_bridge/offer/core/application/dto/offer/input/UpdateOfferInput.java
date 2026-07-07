package br.com.medibridge.medi_bridge.offer.core.application.dto.offer.input;

import java.util.UUID;

public record UpdateOfferInput(
        UUID id,
        ProductInput product
) {
}
