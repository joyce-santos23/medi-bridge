package br.com.medibridge.medi_bridge.transfer.core.application.usecase;

import br.com.medibridge.medi_bridge.shared.application.security.AuthenticatedUser;
import br.com.medibridge.medi_bridge.shared.domain.exception.ForbiddenException;
import br.com.medibridge.medi_bridge.transfer.core.application.dto.response.TransferResponseDTO;
import br.com.medibridge.medi_bridge.transfer.core.application.port.DomainEventPublisherGateway;
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
public class ApproveTransferUseCase {

    private final TransferGateway transferGateway;
    private final DomainEventPublisherGateway domainEventPublisherGateway;

    public TransferResponseDTO execute(AuthenticatedUser currentUser, UUID transferId) {
        log.info("Executing ApproveTransferUseCase for transfer ID: {} by user: {}", transferId, currentUser != null ? currentUser.id() : "anonymous");

        if (currentUser == null) {
            throw new ForbiddenException("Authentication required");
        }

        Transfer transfer = transferGateway.findById(transferId)
                .orElseThrow(() -> new TransferNotFoundException("Transfer request not found"));

        // Apenas o hospital de origem  pode aprovar
        if (!currentUser.hospitalId().equals(transfer.getSourceHospitalId())) {
            throw new ForbiddenException("Only the source hospital can approve this transfer request");
        }

        Instant now = Instant.now();

        transfer.approve(now);

        transferGateway.save(transfer);

        domainEventPublisherGateway.publish(
                transfer.pullDomainEvents()
        );

        return TransferResponseDTO.from(transfer, true);
    }
}
