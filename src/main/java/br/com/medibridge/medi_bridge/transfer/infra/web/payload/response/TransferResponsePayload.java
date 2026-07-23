package br.com.medibridge.medi_bridge.transfer.infra.web.payload.response;

import br.com.medibridge.medi_bridge.offer.infra.web.payload.response.HospitalSummaryResponsePayload;
import br.com.medibridge.medi_bridge.offer.infra.web.payload.response.UserSummaryResponsePayload;
import br.com.medibridge.medi_bridge.offer.infra.web.payload.response.ProductResponsePayload;
import br.com.medibridge.medi_bridge.transfer.core.domain.enums.TransferStatus;
import java.time.Instant;
import java.util.UUID;

public record TransferResponsePayload(
        UUID id,
        TransferStatus status,
        String reason,
        String confirmationCode,
        UUID offerId,
        ProductResponsePayload product,
        HospitalSummaryResponsePayload sourceHospital,
        HospitalSummaryResponsePayload destinationHospital,
        UserSummaryResponsePayload requester,
        Instant createdAt,
        Instant statusChangedAt,
        Instant expiresAt
) {}
