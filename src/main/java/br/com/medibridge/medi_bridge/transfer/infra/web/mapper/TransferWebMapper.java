package br.com.medibridge.medi_bridge.transfer.infra.web.mapper;

import br.com.medibridge.medi_bridge.offer.core.domain.offer.enums.Category;
import br.com.medibridge.medi_bridge.offer.core.domain.offer.enums.Unit;
import br.com.medibridge.medi_bridge.offer.infra.web.payload.response.HospitalAddressResponsePayload;
import br.com.medibridge.medi_bridge.offer.infra.web.payload.response.HospitalSummaryResponsePayload;
import br.com.medibridge.medi_bridge.offer.infra.web.payload.response.UserSummaryResponsePayload;
import br.com.medibridge.medi_bridge.offer.infra.web.payload.response.ProductResponsePayload;
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

        var sourceHosp = dto.sourceHospital();
        HospitalSummaryResponsePayload sourceHospResponse = null;
        if (sourceHosp != null) {
            var addr = sourceHosp.address();
            var addrPayload = addr != null ? new HospitalAddressResponsePayload(
                    addr.zipCode(),
                    addr.street(),
                    addr.neighborhood(),
                    addr.city(),
                    addr.state(),
                    addr.number(),
                    addr.complement()
            ) : null;

            sourceHospResponse = new HospitalSummaryResponsePayload(
                    sourceHosp.id(),
                    sourceHosp.name(),
                    sourceHosp.email(),
                    sourceHosp.phone(),
                    addrPayload
            );
        }

        var destHosp = dto.destinationHospital();
        HospitalSummaryResponsePayload destHospResponse = null;
        if (destHosp != null) {
            var addr = destHosp.address();
            var addrPayload = addr != null ? new HospitalAddressResponsePayload(
                    addr.zipCode(),
                    addr.street(),
                    addr.neighborhood(),
                    addr.city(),
                    addr.state(),
                    addr.number(),
                    addr.complement()
            ) : null;

            destHospResponse = new HospitalSummaryResponsePayload(
                    destHosp.id(),
                    destHosp.name(),
                    destHosp.email(),
                    destHosp.phone(),
                    addrPayload
            );
        }

        var req = dto.requester();
        var reqResponse = req != null ? new UserSummaryResponsePayload(
                req.id(),
                req.name(),
                req.professionalCouncil(),
                req.professionalRegistration()
        ) : null;

        var off = dto.offer();
        ProductResponsePayload productResponse = null;
        if (off != null && off.product() != null) {
            var prod = off.product();
            productResponse = new ProductResponsePayload(
                    prod.name(),
                    Category.valueOf(prod.category()),
                    prod.manufacturer(),
                    prod.batch(),
                    prod.expirationDate(),
                    prod.quantity(),
                    Unit.valueOf(prod.unit()),
                    prod.observations()
            );
        }

        return new TransferResponsePayload(
                dto.id(),
                dto.status(),
                dto.reason(),
                dto.confirmationCode(),
                dto.offerId(),
                productResponse,
                sourceHospResponse,
                destHospResponse,
                reqResponse,
                dto.createdAt(),
                dto.statusChangedAt(),
                dto.expiresAt()
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
