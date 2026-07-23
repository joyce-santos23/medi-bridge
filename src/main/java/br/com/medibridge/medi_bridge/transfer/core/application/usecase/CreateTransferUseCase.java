package br.com.medibridge.medi_bridge.transfer.core.application.usecase;

import br.com.medibridge.medi_bridge.shared.application.security.AuthenticatedUser;
import br.com.medibridge.medi_bridge.shared.domain.exception.ForbiddenException;
import br.com.medibridge.medi_bridge.shared.domain.exception.ValidationException;
import br.com.medibridge.medi_bridge.transfer.core.application.dto.integration.HospitalSummary;
import br.com.medibridge.medi_bridge.transfer.core.application.dto.integration.OfferSummary;
import br.com.medibridge.medi_bridge.transfer.core.application.dto.integration.UserSummary;
import br.com.medibridge.medi_bridge.transfer.core.application.dto.request.CreateTransferRequestDTO;
import br.com.medibridge.medi_bridge.transfer.core.application.dto.response.TransferResponseDTO;
import br.com.medibridge.medi_bridge.transfer.core.application.port.*;
import br.com.medibridge.medi_bridge.transfer.core.domain.entity.Transfer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateTransferUseCase {

    private final TransferGateway transferGateway;
    private final OfferGateway offerGateway;
    private final CatalogGateway catalogGateway;
    private final DomainEventPublisherGateway domainEventPublisherGateway;

    public TransferResponseDTO execute(AuthenticatedUser currentUser, CreateTransferRequestDTO request) {
        log.info("Executing CreateTransferUseCase for user: {}", currentUser != null ? currentUser.id() : "anonymous");

        if (currentUser == null) {
            throw new ForbiddenException("Authentication required");
        }

        UUID destinationHospitalId = currentUser.hospitalId();
        if (destinationHospitalId == null) {
            throw new ValidationException("User must be associated with a hospital to request a transfer");
        }

        // Validar hospital de destino
        HospitalSummary destinationHospital = catalogGateway.findHospitalById(destinationHospitalId)
                .orElseThrow(() -> new ValidationException("Destination hospital does not exist in catalog"));

        if (!destinationHospital.active()) {
            throw new ValidationException("Destination hospital is inactive");
        }

        if (request == null || request.offerId() == null) {
            throw new ValidationException("Offer ID is required");
        }

        // Buscar oferta e validar
        OfferSummary offer = offerGateway.findById(request.offerId())
                .orElseThrow(() -> new ValidationException("Offer not found"));

        UUID sourceHospitalId = offer.hospitalId();

        Instant now = Instant.now();

        Transfer transfer = Transfer.create(
                offer.id(),
                sourceHospitalId,
                destinationHospitalId,
                currentUser.id(),
                now
        );

        offerGateway.reserve(offer.id());

        transferGateway.save(transfer);

        domainEventPublisherGateway.publish(transfer.pullDomainEvents());

        HospitalSummary sourceHospital = catalogGateway.findHospitalById(sourceHospitalId).orElse(null);
        UserSummary requester = catalogGateway.findUserById(currentUser.id()).orElse(null);
        OfferSummary offerSummary = offerGateway.findById(offer.id()).orElse(null);

        return TransferResponseDTO.from(transfer, false, sourceHospital, destinationHospital, requester, offerSummary);
    }
}
