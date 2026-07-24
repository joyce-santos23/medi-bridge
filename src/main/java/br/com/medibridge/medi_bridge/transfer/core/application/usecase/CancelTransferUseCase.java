package br.com.medibridge.medi_bridge.transfer.core.application.usecase;

import br.com.medibridge.medi_bridge.shared.application.security.AuthenticatedUser;
import br.com.medibridge.medi_bridge.shared.domain.exception.ForbiddenException;
import br.com.medibridge.medi_bridge.shared.domain.exception.ValidationException;
import br.com.medibridge.medi_bridge.transfer.core.application.dto.integration.HospitalSummary;
import br.com.medibridge.medi_bridge.transfer.core.application.dto.integration.OfferSummary;
import br.com.medibridge.medi_bridge.transfer.core.application.dto.integration.UserSummary;
import br.com.medibridge.medi_bridge.transfer.core.application.dto.request.CancelTransferRequestDTO;
import br.com.medibridge.medi_bridge.transfer.core.application.dto.response.TransferResponseDTO;
import br.com.medibridge.medi_bridge.transfer.core.application.port.*;
import br.com.medibridge.medi_bridge.transfer.core.domain.exception.TransferNotFoundException;
import br.com.medibridge.medi_bridge.transfer.core.domain.entity.Transfer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CancelTransferUseCase {

    private final TransferGateway transferRepositoryGateway;
    private final OfferGateway offerGateway;
    private final DomainEventPublisherGateway domainEventPublisherGateway;
    private final CatalogGateway catalogGateway;

    public TransferResponseDTO execute(AuthenticatedUser currentUser, UUID transferId, CancelTransferRequestDTO request) {
        log.info("Executing CancelTransferUseCase for transfer ID: {} by user: {}", transferId, currentUser != null ? currentUser.id() : "anonymous");

        if (currentUser == null) {
            throw new ForbiddenException("Authentication required");
        }

        if (request == null || request.reason() == null || request.reason().trim().isEmpty()) {
            throw new ValidationException("Reason is required to cancel a transfer request");
        }

        Transfer transfer = transferRepositoryGateway.findById(transferId)
                .orElseThrow(() -> new TransferNotFoundException("Transfer request not found"));

        // Apenas o hospital de destino (quem solicitou) pode cancelar
        if (!currentUser.hospitalId().equals(transfer.getDestinationHospitalId())) {
            throw new ForbiddenException("Only the destination hospital can cancel this transfer request");
        }

        Instant now = Instant.now();

        // Alterar estado no domínio
        transfer.cancel(request.reason(), now);

        // Devolver oferta ao status AVAILABLE
        offerGateway.release(transfer.getOfferId());

        // Salvar alterações
        Transfer savedTransfer = transferRepositoryGateway.save(transfer);

        // Publicar eventos
        domainEventPublisherGateway.publish(transfer.pullDomainEvents());

        log.info("Successfully cancelled transfer request with ID: {}", savedTransfer.getId());

        HospitalSummary sourceHospital = catalogGateway.findHospitalById(savedTransfer.getSourceHospitalId()).orElse(null);
        HospitalSummary destinationHospital = catalogGateway.findHospitalById(savedTransfer.getDestinationHospitalId()).orElse(null);
        UserSummary requester = catalogGateway.findUserById(savedTransfer.getRequesterUserId()).orElse(null);
        OfferSummary offer = offerGateway.findById(savedTransfer.getOfferId()).orElse(null);

        return TransferResponseDTO.from(savedTransfer, false, sourceHospital, destinationHospital, requester, offer);
    }
}
