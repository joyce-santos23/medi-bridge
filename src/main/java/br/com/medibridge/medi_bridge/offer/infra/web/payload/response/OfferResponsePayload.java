package br.com.medibridge.medi_bridge.offer.infra.web.payload.response;

import br.com.medibridge.medi_bridge.offer.core.domain.offer.enums.OfferStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record OfferResponsePayload(
        UUID id,
        OfferStatus status,
        ProductResponsePayload product,
        HospitalSummaryResponsePayload hospital,
        UserSummaryResponsePayload createdBy,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
