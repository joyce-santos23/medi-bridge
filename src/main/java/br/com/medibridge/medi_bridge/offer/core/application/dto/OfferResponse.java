package br.com.medibridge.medi_bridge.offer.core.application.dto;

import br.com.medibridge.medi_bridge.offer.core.domain.offer.entity.Offer;
import br.com.medibridge.medi_bridge.offer.core.domain.offer.enums.OfferStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record OfferResponse(
        UUID id,
        UUID hospitalId,
        UUID createdByUserId,
        ProductResponse product,
        OfferStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static OfferResponse from(Offer offer) {
        if (offer == null) {
            return null;
        }
        return new OfferResponse(
                offer.getId(),
                offer.getHospitalId(),
                offer.getCreatedByUserId(),
                ProductResponse.from(offer.getProduct()),
                offer.getStatus(),
                offer.getCreatedAt(),
                offer.getUpdatedAt()
        );
    }
}
