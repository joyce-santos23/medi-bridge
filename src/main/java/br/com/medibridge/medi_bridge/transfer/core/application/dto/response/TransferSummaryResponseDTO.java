package br.com.medibridge.medi_bridge.transfer.core.application.dto.response;

import br.com.medibridge.medi_bridge.transfer.core.domain.enums.TransferStatus;
import br.com.medibridge.medi_bridge.transfer.core.domain.entity.Transfer;

import java.time.Instant;
import java.util.UUID;

public record TransferSummaryResponseDTO(
        UUID id,
        UUID offerId,
        UUID sourceHospitalId,
        UUID destinationHospitalId,
        TransferStatus status,
        Instant createdAt
) {
    public static TransferSummaryResponseDTO from(Transfer transfer) {
        if (transfer == null) {
            return null;
        }
        return new TransferSummaryResponseDTO(
                transfer.getId(),
                transfer.getOfferId(),
                transfer.getSourceHospitalId(),
                transfer.getDestinationHospitalId(),
                transfer.getStatus(),
                transfer.getCreatedAt()
        );
    }
}
