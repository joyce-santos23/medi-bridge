package br.com.medibridge.medi_bridge.transfer.core.application.dto.response;

import br.com.medibridge.medi_bridge.transfer.core.domain.enums.TransferStatus;
import br.com.medibridge.medi_bridge.transfer.core.domain.entity.Transfer;
import br.com.medibridge.medi_bridge.transfer.core.application.dto.integration.HospitalSummary;
import br.com.medibridge.medi_bridge.transfer.core.application.dto.integration.UserSummary;
import br.com.medibridge.medi_bridge.transfer.core.application.dto.integration.OfferSummary;

import java.time.Instant;
import java.util.UUID;

public record TransferResponseDTO(
        UUID id,
        TransferStatus status,
        String reason,
        String confirmationCode,
        UUID offerId,
        HospitalSummary sourceHospital,
        HospitalSummary destinationHospital,
        UserSummary requester,
        OfferSummary offer,
        Instant createdAt,
        Instant statusChangedAt,
        Instant expiresAt
) {
    public static TransferResponseDTO from(Transfer transfer) {
        return from(transfer, false);
    }

    public static TransferResponseDTO from(Transfer transfer, boolean showConfirmationCode) {
        return from(transfer, showConfirmationCode, null, null, null, null);
    }

    public static TransferResponseDTO from(
            Transfer transfer,
            boolean showConfirmationCode,
            HospitalSummary sourceHospital,
            HospitalSummary destinationHospital,
            UserSummary requester,
            OfferSummary offer
    ) {
        if (transfer == null) {
            return null;
        }
        return new TransferResponseDTO(
                transfer.getId(),
                transfer.getStatus(),
                transfer.getReason(),
                showConfirmationCode ? transfer.getConfirmationCode() : null,
                transfer.getOfferId(),
                sourceHospital,
                destinationHospital,
                requester,
                offer,
                transfer.getCreatedAt(),
                transfer.getStatusChangedAt(),
                transfer.getExpiresAt()
        );
    }
}
