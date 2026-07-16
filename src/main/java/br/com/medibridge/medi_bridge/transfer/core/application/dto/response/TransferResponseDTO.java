package br.com.medibridge.medi_bridge.transfer.core.application.dto.response;

import br.com.medibridge.medi_bridge.transfer.core.domain.enums.TransferStatus;
import br.com.medibridge.medi_bridge.transfer.core.domain.entity.Transfer;

import java.time.Instant;
import java.util.UUID;

public record TransferResponseDTO(
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
) {
    public static TransferResponseDTO from(Transfer transfer) {
        return from(transfer, false);
    }

    public static TransferResponseDTO from(Transfer transfer, boolean showConfirmationCode) {
        if (transfer == null) {
            return null;
        }
        return new TransferResponseDTO(
                transfer.getId(),
                transfer.getOfferId(),
                transfer.getSourceHospitalId(),
                transfer.getDestinationHospitalId(),
                transfer.getRequesterUserId(),
                transfer.getStatus(),
                transfer.getCreatedAt(),
                transfer.getStatusChangedAt(),
                transfer.getExpiresAt(),
                transfer.getReason(),
                showConfirmationCode ? transfer.getConfirmationCode() : null
        );
    }
}
