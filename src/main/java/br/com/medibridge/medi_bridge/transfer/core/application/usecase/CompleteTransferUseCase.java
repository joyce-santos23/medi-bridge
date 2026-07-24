package br.com.medibridge.medi_bridge.transfer.core.application.usecase;

import br.com.medibridge.medi_bridge.shared.application.security.AuthenticatedUser;
import br.com.medibridge.medi_bridge.shared.domain.exception.ForbiddenException;
import br.com.medibridge.medi_bridge.transfer.core.application.dto.integration.HospitalSummary;
import br.com.medibridge.medi_bridge.transfer.core.application.dto.integration.OfferSummary;
import br.com.medibridge.medi_bridge.transfer.core.application.dto.integration.UserSummary;
import br.com.medibridge.medi_bridge.transfer.core.application.dto.request.CompleteTransferRequestDTO;
import br.com.medibridge.medi_bridge.transfer.core.application.dto.response.TransferResponseDTO;
import br.com.medibridge.medi_bridge.transfer.core.application.port.*;
import br.com.medibridge.medi_bridge.transfer.core.domain.exception.TransferNotFoundException;
import br.com.medibridge.medi_bridge.transfer.core.domain.entity.Transfer;
import br.com.medibridge.medi_bridge.transfer.core.domain.exception.TransferValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompleteTransferUseCase {

    private final TransferGateway transferGateway;
    private final OfferGateway offerGateway;
    private final DomainEventPublisherGateway domainEventPublisherGateway;
    private final CatalogGateway catalogGateway;

    public TransferResponseDTO execute(AuthenticatedUser currentUser, UUID transferId, CompleteTransferRequestDTO request) {
        log.info("Executing CompleteTransferUseCase for transfer ID: {} by user: {}", transferId, currentUser != null ? currentUser.id() : "anonymous");

        if (currentUser == null) {
            throw new ForbiddenException("Authentication required");
        }

        Transfer transfer = transferGateway.findById(transferId)
                .orElseThrow(() -> new TransferNotFoundException("Transfer request not found"));

        // Apenas o hospital de destino (quem solicitou e recebeu os itens) pode completar
        if (!currentUser.hospitalId().equals(transfer.getDestinationHospitalId())) {
            throw new ForbiddenException("Only the destination hospital can complete this transfer");
        }

        if (request == null || request.confirmationCode() == null || request.confirmationCode().trim().isEmpty()) {
            throw new TransferValidationException("Confirmation code is required");
        }

        Instant now = Instant.now();

        // Alterar estado no domínio
        transfer.complete(request.confirmationCode(), now);

        offerGateway.complete(transfer.getOfferId());

        Transfer savedTransfer = transferGateway.save(transfer);

        domainEventPublisherGateway.publish(transfer.pullDomainEvents());

        log.info("Successfully completed transfer request with ID: {}", savedTransfer.getId());

        HospitalSummary sourceHospital = catalogGateway.findHospitalById(savedTransfer.getSourceHospitalId()).orElse(null);
        HospitalSummary destinationHospital = catalogGateway.findHospitalById(savedTransfer.getDestinationHospitalId()).orElse(null);
        UserSummary requester = catalogGateway.findUserById(savedTransfer.getRequesterUserId()).orElse(null);
        OfferSummary offer = offerGateway.findById(savedTransfer.getOfferId()).orElse(null);

        return TransferResponseDTO.from(savedTransfer, false, sourceHospital, destinationHospital, requester, offer);
    }
}
