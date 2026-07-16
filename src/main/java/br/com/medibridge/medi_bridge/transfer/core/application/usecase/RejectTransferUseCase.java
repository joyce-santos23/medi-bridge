package br.com.medibridge.medi_bridge.transfer.core.application.usecase;

import br.com.medibridge.medi_bridge.shared.application.security.AuthenticatedUser;
import br.com.medibridge.medi_bridge.shared.domain.exception.ForbiddenException;
import br.com.medibridge.medi_bridge.shared.domain.exception.ValidationException;
import br.com.medibridge.medi_bridge.transfer.core.application.dto.request.RejectTransferRequestDTO;
import br.com.medibridge.medi_bridge.transfer.core.application.dto.response.TransferResponseDTO;
import br.com.medibridge.medi_bridge.transfer.core.application.port.DomainEventPublisherGateway;
import br.com.medibridge.medi_bridge.transfer.core.application.port.OfferGateway;
import br.com.medibridge.medi_bridge.transfer.core.application.port.TransferGateway;
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
public class RejectTransferUseCase {

    private final TransferGateway transferGateway;
    private final OfferGateway offerGateway;
    private final DomainEventPublisherGateway domainEventPublisherGateway;

    public TransferResponseDTO execute(AuthenticatedUser currentUser, UUID transferId, RejectTransferRequestDTO request) {
        log.info("Executing RejectTransferUseCase for transfer ID: {} by user: {}", transferId, currentUser != null ? currentUser.id() : "anonymous");

        if (currentUser == null) {
            throw new ForbiddenException("Authentication required");
        }

        Transfer transfer = transferGateway.findById(transferId)
                .orElseThrow(() -> new TransferNotFoundException("Transfer request not found"));

        if (!currentUser.hospitalId().equals(transfer.getSourceHospitalId())) {
            throw new ForbiddenException("Only the source hospital can reject this transfer request");
        }

        Instant now = Instant.now();

        transfer.reject(request.reason(), now);

        offerGateway.release(transfer.getOfferId());

        transferGateway.save(transfer);

        domainEventPublisherGateway.publish(transfer.pullDomainEvents());

        log.info("Successfully rejected transfer request with ID: {}", transfer.getId());
        return TransferResponseDTO.from(transfer);
    }
}
