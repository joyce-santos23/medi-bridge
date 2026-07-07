package br.com.medibridge.medi_bridge.offer.core.application.dto.offer.output;

import br.com.medibridge.medi_bridge.offer.core.domain.offer.entity.Offer;
import br.com.medibridge.medi_bridge.offer.core.domain.offer.enums.OfferStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record OfferOutput(
        UUID id,
        UUID hospitalId,
        UUID createdByUserId,
        ProductOutput product,
        OfferStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static OfferOutput from(Offer offer) {
        if (offer == null) {
            return null;
        }
        return new OfferOutput(
                offer.getId(),
                offer.getHospitalId(),
                offer.getCreatedByUserId(),
                ProductOutput.from(offer.getProduct()),
                offer.getStatus(),
                offer.getCreatedAt(),
                offer.getUpdatedAt()
        );
    }
}
