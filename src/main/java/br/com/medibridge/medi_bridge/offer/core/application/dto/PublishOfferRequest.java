package br.com.medibridge.medi_bridge.offer.core.application.dto;

import br.com.medibridge.medi_bridge.offer.core.domain.offer.enums.Category;
import br.com.medibridge.medi_bridge.offer.core.domain.offer.enums.Unit;
import java.time.LocalDate;

public record PublishOfferRequest(
        ProductRequest product
) {
    public record ProductRequest(
            String name,
            Category category,
            String manufacturer,
            String batch,
            LocalDate expirationDate,
            Integer quantity,
            Unit unit,
            String observations
    ) {}
}
