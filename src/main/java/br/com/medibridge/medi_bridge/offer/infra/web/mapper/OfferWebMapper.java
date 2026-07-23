package br.com.medibridge.medi_bridge.offer.infra.web.mapper;

import br.com.medibridge.medi_bridge.offer.core.application.dto.OfferResponseDTO;
import br.com.medibridge.medi_bridge.offer.core.application.dto.PublishOfferRequestDTO;
import br.com.medibridge.medi_bridge.offer.core.application.dto.UpdateOfferRequestDTO;
import br.com.medibridge.medi_bridge.offer.infra.web.payload.request.PublishOfferRequestPayload;
import br.com.medibridge.medi_bridge.offer.infra.web.payload.request.UpdateOfferRequestPayload;
import br.com.medibridge.medi_bridge.offer.infra.web.payload.response.*;

import java.util.UUID;

public final class OfferWebMapper {

    private OfferWebMapper() {
    }

    public static PublishOfferRequestDTO toDTO(PublishOfferRequestPayload payload) {
        if (payload == null) {
            return null;
        }

        var prod = payload.product();
        var prodDTO = new PublishOfferRequestDTO.ProductRequestDTO(
                prod.name(),
                prod.category(),
                prod.manufacturer(),
                prod.batch(),
                prod.expirationDate(),
                prod.quantity(),
                prod.unit(),
                prod.observations()
        );

        return new PublishOfferRequestDTO(prodDTO);
    }

    public static UpdateOfferRequestDTO toDTO(UUID id, UpdateOfferRequestPayload payload) {
        if (payload == null) {
            return null;
        }

        var prod = payload.product();
        var prodDTO = new UpdateOfferRequestDTO.ProductRequestDTO(
                prod.name(),
                prod.category(),
                prod.manufacturer(),
                prod.batch(),
                prod.expirationDate(),
                prod.quantity(),
                prod.unit(),
                prod.observations()
        );

        return new UpdateOfferRequestDTO(id, prodDTO);
    }

    public static OfferResponsePayload toResponse(OfferResponseDTO dto) {
        if (dto == null) {
            return null;
        }

        var prod = dto.product();
        var prodResponse = prod != null ? new ProductResponsePayload(
                prod.name(),
                prod.category(),
                prod.manufacturer(),
                prod.batch(),
                prod.expirationDate(),
                prod.quantity(),
                prod.unit(),
                prod.observations()
        ) : null;

        var hosp = dto.hospital();
        HospitalSummaryResponsePayload hospResponse = null;
        if (hosp != null) {
            var addr = hosp.address();
            var addrPayload = addr != null ? new HospitalAddressResponsePayload(
                    addr.zipCode(),
                    addr.street(),
                    addr.neighborhood(),
                    addr.city(),
                    addr.state(),
                    addr.number(),
                    addr.complement()
            ) : null;

            hospResponse = new HospitalSummaryResponsePayload(
                    hosp.id(),
                    hosp.name(),
                    hosp.email(),
                    hosp.phone(),
                    addrPayload
            );
        }

        var creator = dto.createdBy();
        var creatorResponse = creator != null ? new UserSummaryResponsePayload(
                creator.id(),
                creator.name(),
                creator.professionalCouncil(),
                creator.professionalRegistration()
        ) : null;

        return new OfferResponsePayload(
                dto.id(),
                dto.status(),
                prodResponse,
                hospResponse,
                creatorResponse,
                dto.createdAt(),
                dto.updatedAt()
        );
    }
}