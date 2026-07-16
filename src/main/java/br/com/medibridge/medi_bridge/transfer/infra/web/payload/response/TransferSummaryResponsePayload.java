package br.com.medibridge.medi_bridge.transfer.infra.web.payload.response;

import br.com.medibridge.medi_bridge.transfer.core.domain.enums.TransferStatus;
import java.time.Instant;
import java.util.UUID;

public record TransferSummaryResponsePayload(
        UUID id,
        UUID offerId,
        UUID sourceHospitalId,
        UUID destinationHospitalId,
        TransferStatus status,
        Instant createdAt
) {}
