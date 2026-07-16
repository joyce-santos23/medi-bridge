package br.com.medibridge.medi_bridge.transfer.infra.web.mapper;

import br.com.medibridge.medi_bridge.transfer.core.application.dto.request.CreateTransferRequestDTO;
import br.com.medibridge.medi_bridge.transfer.core.application.dto.request.CancelTransferRequestDTO;
import br.com.medibridge.medi_bridge.transfer.core.application.dto.request.RejectTransferRequestDTO;
import br.com.medibridge.medi_bridge.transfer.core.application.dto.request.CompleteTransferRequestDTO;
import br.com.medibridge.medi_bridge.transfer.core.application.dto.response.TransferResponseDTO;
import br.com.medibridge.medi_bridge.transfer.core.application.dto.response.TransferSummaryResponseDTO;
import br.com.medibridge.medi_bridge.transfer.infra.web.payload.request.CreateTransferRequestPayload;
import br.com.medibridge.medi_bridge.transfer.infra.web.payload.request.CancelTransferRequestPayload;
import br.com.medibridge.medi_bridge.transfer.infra.web.payload.request.RejectTransferRequestPayload;
import br.com.medibridge.medi_bridge.transfer.infra.web.payload.request.CompleteTransferRequestPayload;
import br.com.medibridge.medi_bridge.transfer.infra.web.payload.response.TransferResponsePayload;
import br.com.medibridge.medi_bridge.transfer.infra.web.payload.response.TransferSummaryResponsePayload;

public final class TransferWebMapper {

    private TransferWebMapper() {}

    public static CreateTransferRequestDTO toDTO(CreateTransferRequestPayload payload) {
        if (payload == null) {
            return null;
        }
        return new CreateTransferRequestDTO(payload.offerId(), payload.reason());
    }

    public static CancelTransferRequestDTO toDTO(CancelTransferRequestPayload payload) {
        if (payload == null) {
            return null;
        }
        return new CancelTransferRequestDTO(payload.reason());
    }

    public static RejectTransferRequestDTO toDTO(RejectTransferRequestPayload payload) {
        if (payload == null) {
            return null;
        }
        return new RejectTransferRequestDTO(payload.reason());
    }

    public static CompleteTransferRequestDTO toDTO(CompleteTransferRequestPayload payload) {
        if (payload == null) {
            return null;
        }
        return new CompleteTransferRequestDTO(payload.confirmationCode());
    }

    public static TransferResponsePayload toResponse(TransferResponseDTO dto) {
        if (dto == null) {
            return null;
        }
        return new TransferResponsePayload(
                dto.id(),
                dto.offerId(),
                dto.sourceHospitalId(),
                dto.destinationHospitalId(),
                dto.requesterUserId(),
                dto.status(),
                dto.createdAt(),
                dto.statusChangedAt(),
                dto.expiresAt(),
                dto.reason(),
                dto.confirmationCode()
        );
    }

    public static TransferSummaryResponsePayload toResponse(TransferSummaryResponseDTO dto) {
        if (dto == null) {
            return null;
        }
        return new TransferSummaryResponsePayload(
                dto.id(),
                dto.offerId(),
                dto.sourceHospitalId(),
                dto.destinationHospitalId(),
                dto.status(),
                dto.createdAt()
        );
    }
}
