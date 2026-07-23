package br.com.medibridge.medi_bridge.transfer.core.application.usecase;

import br.com.medibridge.medi_bridge.shared.application.security.AuthenticatedUser;
import br.com.medibridge.medi_bridge.shared.domain.exception.ForbiddenException;
import br.com.medibridge.medi_bridge.shared.domain.exception.ValidationException;
import br.com.medibridge.medi_bridge.transfer.core.application.dto.response.TransferSummaryResponseDTO;
import br.com.medibridge.medi_bridge.transfer.core.application.port.TransferGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ListPendingApprovalTransfersUseCase {

    private final TransferGateway transferGateway;

    public List<TransferSummaryResponseDTO> execute(AuthenticatedUser currentUser) {
        log.info("Executing ListPendingApprovalTransfersUseCase for user: {}", currentUser != null ? currentUser.id() : "anonymous");

        if (currentUser == null) {
            throw new ForbiddenException("Authentication required");
        }

        UUID hospitalId = currentUser.hospitalId();
        if (hospitalId == null) {
            throw new ValidationException("User must be associated with a hospital to list transfers");
        }

        log.info("Listing pending approval transfers for source hospital ID: {}", hospitalId);

        return transferGateway.findPendingApprovalsBySourceHospital(hospitalId).stream()
                .map(TransferSummaryResponseDTO::from)
                .toList();
    }
}
