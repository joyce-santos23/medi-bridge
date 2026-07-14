package br.com.medibridge.medi_bridge.offer.core.application.dto;

import br.com.medibridge.medi_bridge.offer.core.domain.offer.entity.Offer;
import br.com.medibridge.medi_bridge.offer.core.domain.offer.enums.OfferStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record OfferResponseDTO(
        UUID id,
        UUID hospitalId,
        UUID createdByUserId,
        ProductResponseDTO product,
        OfferStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static OfferResponseDTO from(Offer offer) {
        if (offer == null) {
            return null;
        }
        return new OfferResponseDTO(
                offer.getId(),
                offer.getHospitalId(),
                offer.getCreatedByUserId(),
                ProductResponseDTO.from(offer.getProduct()),
                offer.getStatus(),
                offer.getCreatedAt(),
                offer.getUpdatedAt()
        );
    }
}
