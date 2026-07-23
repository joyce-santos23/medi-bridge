package br.com.medibridge.medi_bridge.offer.core.application.dto;

import br.com.medibridge.medi_bridge.offer.core.domain.offer.entity.Offer;
import br.com.medibridge.medi_bridge.offer.core.domain.offer.enums.OfferStatus;
import br.com.medibridge.medi_bridge.offer.core.application.dto.integration.HospitalSummary;
import br.com.medibridge.medi_bridge.offer.core.application.dto.integration.UserSummary;
import java.time.LocalDateTime;
import java.util.UUID;

public record OfferResponseDTO(
        UUID id,
        OfferStatus status,
        ProductResponseDTO product,
        HospitalSummary hospital,
        UserSummary createdBy,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static OfferResponseDTO from(Offer offer) {
        return from(offer, null, null);
    }

    public static OfferResponseDTO from(
            Offer offer,
            HospitalSummary hospital,
            UserSummary createdBy
    ) {
        if (offer == null) {
            return null;
        }
        return new OfferResponseDTO(
                offer.getId(),
                offer.getStatus(),
                ProductResponseDTO.from(offer.getProduct()),
                hospital,
                createdBy,
                offer.getCreatedAt(),
                offer.getUpdatedAt()
        );
    }
}
