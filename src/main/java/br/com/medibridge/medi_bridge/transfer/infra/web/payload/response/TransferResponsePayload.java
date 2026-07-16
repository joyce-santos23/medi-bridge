package br.com.medibridge.medi_bridge.transfer.infra.web.payload.response;

import br.com.medibridge.medi_bridge.transfer.core.domain.enums.TransferStatus;
import java.time.Instant;
import java.util.UUID;

public record TransferResponsePayload(
        UUID id,
        UUID offerId,
        UUID sourceHospitalId,
        UUID destinationHospitalId,
        UUID requesterUserId,
        TransferStatus status,
        Instant createdAt,
        Instant statusChangedAt,
        Instant expiresAt,
        String reason,
        String confirmationCode
) {}
